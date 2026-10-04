package pe.edu.upc.latia_lati;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import pe.edu.upc.latia_lati.entities.Role;
import pe.edu.upc.latia_lati.repositories.IUsersRepository;

import static org.assertj.core.api.Assertions.assertThat;

/** HTTP real + filtros JWT + servicios + Hibernate, sobre una BD de prueba aislada. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class Hu01To10IntegrationTests {
    @LocalServerPort int port;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired IUsersRepository users;
    @Autowired PasswordEncoder encoder;
    private final HttpClient http = HttpClient.newHttpClient();
    private static final String PASSWORD = "Latia.Test123!";

    static class Result {
        private int status;

        private JsonNode body;

        private String text;

        private String location;

        public Result() {
        }

        public Result(int status, JsonNode body, String text, String location) {
            this.status = status;
            this.body = body;
            this.text = text;
            this.location = location;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public JsonNode getBody() {
            return body;
        }

        public void setBody(JsonNode body) {
            this.body = body;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

    }
    static class Account {
        private long id;

        private String username;

        private String token;

        public Account() {
        }

        public Account(long id, String username, String token) {
            this.id = id;
            this.username = username;
            this.token = token;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

    @BeforeEach
    void cleanDatabase() {
        jdbc.update("DELETE FROM emergency_contacts");
        jdbc.update("DELETE FROM health_profiles");
        jdbc.update("DELETE FROM roles");
        jdbc.update("DELETE FROM users");
    }

    private Result call(String method, String path, String token, Object body) throws Exception {
        var b = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
        if (token != null) b.header("Authorization", "Bearer " + token);
        if (body != null) b.header("Content-Type", "application/json");
        b.method(method, body == null ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(json.writeValueAsString(body)));
        var response = http.send(b.build(), HttpResponse.BodyHandlers.ofString());
        var tree = response.body().isBlank() ? json.createObjectNode() : json.readTree(response.body());
        return new Result(response.statusCode(), tree, response.body(), response.headers().firstValue("Location").orElse(""));
    }

    private Map<String, Object> registration(String username) {
        return new LinkedHashMap<>(Map.of("firstName", "Ana", "lastName", "Prueba", "username", username,
                "email", username + "@example.test", "password", PASSWORD));
    }

    private Account account(String username, boolean admin) throws Exception {
        Result r = call("POST", "/api/users", null, registration(username));
        assertThat(r.getStatus()).as(r.getText()).isEqualTo(201);
        long id = r.getBody().get("idUser").asLong();
        if (admin) {
            var u = users.findById(id).orElseThrow();
            var role = new Role(); role.setRol("ROLE_ADMIN"); role.setUser(u); u.getRoles().add(role);
            users.saveAndFlush(u);
        }
        return new Account(id, username, login(username, PASSWORD));
    }

    private String login(String username, String password) throws Exception {
        Result r = call("POST", "/login", null, Map.of("username", username, "password", password));
        assertThat(r.getStatus()).as(r.getText()).isEqualTo(200);
        return r.getBody().get("token").asText();
    }

    private Map<String, Object> profile() {
        return new LinkedHashMap<>(Map.of("firstName", "Lucía", "lastName", "Prueba", "birthDate", "2015-03-10",
                "sex", "Femenino", "bloodType", "O+", "phone", "+51 999999999", "active", true));
    }

    private long createProfile(Account a) throws Exception {
        Result r = call("POST", "/api/healthprofiles", a.getToken(), profile());
        assertThat(r.getStatus()).as(r.getText()).isEqualTo(201);
        return r.getBody().get("idHealthProfile").asLong();
    }

    @Test void hu01CreatesUserWithHashRoleAndServerOwnedFields() throws Exception {
        var payload = registration("alice");
        payload.put("idUser", 999999); payload.put("active", false);
        payload.put("roles", new String[]{"ROLE_ADMIN"}); payload.put("createdAt", "2000-01-01");
        Result r = call("POST", "/api/users", null, payload);
        assertThat(r.getStatus()).as(r.getText()).isEqualTo(201);
        long id = r.getBody().get("idUser").asLong();
        assertThat(id).isNotEqualTo(999999);
        assertThat(r.getLocation()).endsWith("/api/users/" + id);
        assertThat(r.getBody().get("active").asBoolean()).isTrue();
        assertThat(java.time.OffsetDateTime.parse(r.getBody().get("createdAt").asText()).toLocalDate()).isEqualTo(LocalDate.now().toString());
        assertThat(r.getText()).doesNotContain("passwordHash", "ROLE_ADMIN", PASSWORD);
        var u = users.findById(id).orElseThrow();
        assertThat(encoder.matches(PASSWORD, u.getPasswordHash())).isTrue();
        assertThat(u.getRoles()).extracting(Role::getRol).containsExactly("ROLE_USER");
    }

    @Test void hu01RejectsInvalidAndDuplicateAccounts() throws Exception {
        var bad = registration("alice"); bad.put("email", "not-an-email"); bad.put("firstName", " ");
        assertThat(call("POST", "/api/users", null, bad).getStatus()).isEqualTo(400);
        account("alice", false);
        assertThat(call("POST", "/api/users", null, registration("alice")).getStatus()).isEqualTo(409);
        var duplicateEmail = registration("other"); duplicateEmail.put("email", "ALICE@example.test");
        assertThat(call("POST", "/api/users", null, duplicateEmail).getStatus()).isEqualTo(409);
    }

    @Test void userRoutesRequireAuthenticationExceptRegistration() throws Exception {
        assertThat(call("GET", "/api/users", null, null).getStatus()).isEqualTo(401);
        assertThat(call("PUT", "/api/users/1", null, registration("alice")).getStatus()).isEqualTo(401);
        assertThat(call("DELETE", "/api/users/1", null, null).getStatus()).isEqualTo(401);
    }

    @Test void hu02OnlyAdminCanListUsersAndNoHashLeaks() throws Exception {
        Account a = account("alice", false);
        Account admin = account("admin", true);
        assertThat(call("GET", "/api/users", a.getToken(), null).getStatus()).isEqualTo(403);
        Result r = call("GET", "/api/users", admin.getToken(), null);
        assertThat(r.getStatus()).isEqualTo(200);
        assertThat(r.getBody().size()).isEqualTo(2);
        assertThat(r.getText()).doesNotContain("passwordHash", PASSWORD);
    }

    @Test void hu03FindsOwnUserAndReturns404ForMissingIdToAdmin() throws Exception {
        Account a = account("alice", false);
        Account admin = account("admin", true);
        Result own = call("GET", "/api/users/" + a.getId(), a.getToken(), null);
        assertThat(own.getStatus()).isEqualTo(200);
        assertThat(own.getBody().get("idUser").asLong()).isEqualTo(a.getId());
        assertThat(call("GET", "/api/users/9999999", admin.getToken(), null).getStatus()).isEqualTo(404);
        assertThat(call("GET", "/api/users/" + admin.getId(), a.getToken(), null).getStatus()).isEqualTo(403);
    }

    @Test void hu04UpdatesByPathIdWithoutChangingPasswordOrRole() throws Exception {
        Account a = account("alice", false);
        String hash = users.findById(a.getId()).orElseThrow().getPasswordHash();
        var payload = registration("alice"); payload.remove("password"); payload.put("firstName", "Alicia");
        payload.put("idUser", 999999); payload.put("active", false); payload.put("roles", new String[]{"ROLE_ADMIN"});
        Result r = call("PUT", "/api/users/" + a.getId(), a.getToken(), payload);
        assertThat(r.getStatus()).as(r.getText()).isEqualTo(200);
        assertThat(r.getBody().get("firstName").asText()).isEqualTo("Alicia");
        var saved = users.findById(a.getId()).orElseThrow();
        assertThat(saved.getPasswordHash()).isEqualTo(hash);
        assertThat(saved.getActive()).isTrue();
        assertThat(saved.getRoles()).extracting(Role::getRol).containsExactly("ROLE_USER");
    }

    @Test void hu04RejectsForeignChangesInvalidDataAndDuplicates() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        assertThat(call("PUT", "/api/users/" + b.getId(), a.getToken(), registration("alice")).getStatus()).isEqualTo(403);
        var bad = registration("alice"); bad.put("lastName", "");
        assertThat(call("PUT", "/api/users/" + a.getId(), a.getToken(), bad).getStatus()).isEqualTo(400);
        assertThat(call("PUT", "/api/users/" + a.getId(), a.getToken(), registration("bob")).getStatus()).isEqualTo(409);
    }

    @Test void hu05DeletesAccountAndReturns404WhenAdminRepeats() throws Exception {
        Account a = account("alice", false); Account admin = account("admin", true);
        assertThat(call("DELETE", "/api/users/" + a.getId(), a.getToken(), null).getStatus()).isEqualTo(204);
        assertThat(users.existsById(a.getId())).isFalse();
        assertThat(call("DELETE", "/api/users/" + a.getId(), admin.getToken(), null).getStatus()).isEqualTo(404);
    }

    @Test void hu05BlocksDeletionWithProfilesAndRollsBack() throws Exception {
        Account a = account("alice", false); createProfile(a);
        assertThat(call("DELETE", "/api/users/" + a.getId(), a.getToken(), null).getStatus()).isEqualTo(409);
        var saved = users.findById(a.getId()).orElseThrow();
        assertThat(saved.getRoles()).extracting(Role::getRol).containsExactly("ROLE_USER");
        assertThat(call("GET", "/api/healthprofiles", a.getToken(), null).getStatus()).isEqualTo(200);
    }

    @Test void hu06CreatesNamedDependentWithOwnerFromJwtAndCorrectMapping() throws Exception {
        Account a = account("alice", false);
        var payload = profile(); payload.put("idHealthProfile", 999999); payload.put("createdAt", "2000-01-01T00:00:00Z");
        Result r = call("POST", "/api/healthprofiles", a.getToken(), payload);
        assertThat(r.getStatus()).as(r.getText()).isEqualTo(201);
        long id = r.getBody().get("idHealthProfile").asLong();
        assertThat(id).isNotEqualTo(999999);
        assertThat(r.getLocation()).endsWith("/api/healthprofiles/" + id);
        assertThat(r.getBody().get("idOwnerUser").asLong()).isEqualTo(a.getId());
        assertThat(r.getBody().get("idHolderUser").isNull()).isTrue();
        assertThat(r.getBody().get("firstName").asText()).isEqualTo("Lucía");
    }

    @Test void hu06RejectsInvalidProfilesAndForeignLinks() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        var bad = profile(); bad.put("birthDate", LocalDate.now().plusDays(1).toString());
        assertThat(call("POST", "/api/healthprofiles", a.getToken(), bad).getStatus()).isEqualTo(400);
        bad = profile(); bad.put("bloodType", "INVALID");
        assertThat(call("POST", "/api/healthprofiles", a.getToken(), bad).getStatus()).isEqualTo(400);
        bad = profile(); bad.put("idOwnerUser", b.getId());
        assertThat(call("POST", "/api/healthprofiles", a.getToken(), bad).getStatus()).isEqualTo(403);
        bad = profile(); bad.put("idHolderUser", b.getId());
        assertThat(call("POST", "/api/healthprofiles", a.getToken(), bad).getStatus()).isEqualTo(403);
    }

    @Test void hu06MapsHolderAndRejectsSecondPersonalProfile() throws Exception {
        Account a = account("alice", false);
        var p = profile(); p.put("idHolderUser", a.getId());
        Result r = call("POST", "/api/healthprofiles", a.getToken(), p);
        assertThat(r.getStatus()).isEqualTo(201);
        assertThat(r.getBody().get("idHolderUser").asLong()).isEqualTo(a.getId());
        assertThat(call("POST", "/api/healthprofiles", a.getToken(), p).getStatus()).isEqualTo(409);
    }

    @Test void hu07Returns404WhenEmptyAndOnlyListsOwnProfiles() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        createProfile(b);
        assertThat(call("GET", "/api/healthprofiles", a.getToken(), null).getStatus()).isEqualTo(404);
        long id = createProfile(a);
        Result r = call("GET", "/api/healthprofiles", a.getToken(), null);
        assertThat(r.getStatus()).isEqualTo(200);
        assertThat(r.getBody().size()).isEqualTo(1);
        assertThat(r.getBody().get(0).get("idHealthProfile").asLong()).isEqualTo(id);
    }

    @Test void hu08FindsOwnProfileButNotForeignOrMissing() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        long id = createProfile(a);
        assertThat(call("GET", "/api/healthprofiles/" + id, a.getToken(), null).getStatus()).isEqualTo(200);
        assertThat(call("GET", "/api/healthprofiles/" + id, b.getToken(), null).getStatus()).isEqualTo(404);
        assertThat(call("GET", "/api/healthprofiles/9999999", a.getToken(), null).getStatus()).isEqualTo(404);
    }

    @Test void hu09UpdatesProfileUsingPathIdAndPreservesOwnership() throws Exception {
        Account a = account("alice", false);
        long id = createProfile(a);
        var updated = profile(); updated.put("phone", "+51 988888888"); updated.put("idHealthProfile", 999999);
        Result r = call("PUT", "/api/healthprofiles/" + id, a.getToken(), updated);
        assertThat(r.getStatus()).as(r.getText()).isEqualTo(200);
        assertThat(r.getBody().get("idHealthProfile").asLong()).isEqualTo(id);
        assertThat(r.getBody().get("idOwnerUser").asLong()).isEqualTo(a.getId());
        assertThat(r.getBody().get("phone").asText()).isEqualTo("+51 988888888");
        assertThat(call("GET", "/api/healthprofiles/" + id, a.getToken(), null).getBody().get("phone").asText()).isEqualTo("+51 988888888");
    }

    @Test void hu09RejectsOwnerTransferAndForeignUpdates() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        long id = createProfile(a);
        var transfer = profile(); transfer.put("idOwnerUser", b.getId());
        assertThat(call("PUT", "/api/healthprofiles/" + id, a.getToken(), transfer).getStatus()).isEqualTo(403);
        assertThat(call("PUT", "/api/healthprofiles/" + id, b.getToken(), profile()).getStatus()).isEqualTo(404);
        var bad = profile(); bad.put("phone", "invalid");
        assertThat(call("PUT", "/api/healthprofiles/" + id, a.getToken(), bad).getStatus()).isEqualTo(400);
        assertThat(call("PUT", "/api/healthprofiles/9999999", a.getToken(), profile()).getStatus()).isEqualTo(404);
    }

    @Test void hu10DeletesOwnProfileAndSubsequentGetIs404() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        long id = createProfile(a);
        assertThat(call("DELETE", "/api/healthprofiles/" + id, b.getToken(), null).getStatus()).isEqualTo(404);
        assertThat(call("DELETE", "/api/healthprofiles/" + id, a.getToken(), null).getStatus()).isEqualTo(204);
        assertThat(call("GET", "/api/healthprofiles/" + id, a.getToken(), null).getStatus()).isEqualTo(404);
        assertThat(call("DELETE", "/api/healthprofiles/" + id, a.getToken(), null).getStatus()).isEqualTo(404);
    }

    @Test void hu10Returns409ForRelatedDataWithoutDeletingEitherRecord() throws Exception {
        Account a = account("alice", false); long id = createProfile(a);
        jdbc.update("INSERT INTO emergency_contacts (id_health_profile, name, phone, primary_contact, created_at, updated_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                id, "Familiar", "999999999", true);
        assertThat(call("DELETE", "/api/healthprofiles/" + id, a.getToken(), null).getStatus()).isEqualTo(409);
        assertThat(call("GET", "/api/healthprofiles/" + id, a.getToken(), null).getStatus()).isEqualTo(200);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM emergency_contacts", Long.class)).isEqualTo(1);
    }

    @Test void rejectsNonPositivePathIds() throws Exception {
        Account a = account("alice", false);
        assertThat(call("GET", "/api/users/0", a.getToken(), null).getStatus()).isEqualTo(400);
        assertThat(call("GET", "/api/healthprofiles/-1", a.getToken(), null).getStatus()).isEqualTo(400);
    }

    @Test void changingPasswordRequiresOldPasswordAndDoesNotAlterOtherAccount() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        var request = Map.of("currentPassword", PASSWORD, "newPassword", "Changed.Test123!");
        assertThat(call("PUT", "/api/users/" + b.getId() + "/password", a.getToken(), request).getStatus()).isEqualTo(403);
        assertThat(call("PUT", "/api/users/" + a.getId() + "/password", a.getToken(), Map.of("currentPassword", "wrong", "newPassword", "Changed.Test123!")).getStatus()).isEqualTo(400);
        assertThat(call("PUT", "/api/users/" + a.getId() + "/password", a.getToken(), request).getStatus()).isEqualTo(204);
        assertThat(call("POST", "/login", null, Map.of("username", "alice", "password", PASSWORD)).getStatus()).isEqualTo(401);
        assertThat(login("alice", "Changed.Test123!")).isNotBlank();
    }

    @Test void deletedAccountTokenCannotBecomeANewAccountWithSameUsername() throws Exception {
        Account a = account("alice", false);
        assertThat(call("DELETE", "/api/users/" + a.getId(), a.getToken(), null).getStatus()).isEqualTo(204);
        Account newAlice = account("alice", false);
        assertThat(newAlice.getId()).isNotEqualTo(a.getId());
        assertThat(call("GET", "/api/users/" + newAlice.getId(), a.getToken(), null).getStatus()).isEqualTo(401);
    }

    @Test void disabledAccountAndRemovedAdminRoleTakeEffectOnExistingToken() throws Exception {
        Account admin = account("admin", true);
        jdbc.update("DELETE FROM roles WHERE id_user = ? AND rol = 'ROLE_ADMIN'", admin.getId());
        assertThat(call("GET", "/api/users", admin.getToken(), null).getStatus()).isEqualTo(403);
        jdbc.update("UPDATE users SET active = false WHERE id_user = ?", admin.getId());
        assertThat(call("GET", "/api/users/" + admin.getId(), admin.getToken(), null).getStatus()).isEqualTo(401);
    }

    @Test void postWithExistingIdNeverOverwritesExistingAccountOrProfile() throws Exception {
        Account a = account("alice", false);
        var payload = registration("bob"); payload.put("idUser", a.getId());
        Result created = call("POST", "/api/users", null, payload);
        assertThat(created.getStatus()).isEqualTo(201);
        assertThat(created.getBody().get("idUser").asLong()).isNotEqualTo(a.getId());
        assertThat(users.findById(a.getId()).orElseThrow().getUsername()).isEqualTo("alice");
        long id = createProfile(a);
        var p = profile(); p.put("idHealthProfile", id); p.put("firstName", "Otro paciente");
        Result newProfile = call("POST", "/api/healthprofiles", a.getToken(), p);
        assertThat(newProfile.getStatus()).isEqualTo(201);
        assertThat(newProfile.getBody().get("idHealthProfile").asLong()).isNotEqualTo(id);
        assertThat(call("GET", "/api/healthprofiles/" + id, a.getToken(), null).getBody().get("firstName").asText()).isEqualTo("Lucía");
    }

    @Test void changingUsernameRequiresFreshLoginAndMissingUpdateIdIs404ForAdmin() throws Exception {
        Account a = account("alice", false); Account admin = account("admin", true);
        var payload = registration("alice2");
        assertThat(call("PUT", "/api/users/" + a.getId(), a.getToken(), payload).getStatus()).isEqualTo(200);
        assertThat(call("GET", "/api/users/" + a.getId(), a.getToken(), null).getStatus()).isEqualTo(401);
        String fresh = login("alice2", PASSWORD);
        assertThat(call("GET", "/api/users/" + a.getId(), fresh, null).getStatus()).isEqualTo(200);
        assertThat(call("PUT", "/api/users/9999999", admin.getToken(), registration("unused")).getStatus()).isEqualTo(404);
    }

    @Test void passwordValidationCountsUtf8BytesAndSupportsLegacyAlias() throws Exception {
        var invalid = registration("alice"); invalid.put("password", "é".repeat(40));
        assertThat(call("POST", "/api/users", null, invalid).getStatus()).isEqualTo(400);
        var legacy = registration("legacy"); legacy.remove("password"); legacy.put("passwordHash", PASSWORD);
        assertThat(call("POST", "/api/users", null, legacy).getStatus()).isEqualTo(201);
        assertThat(login("legacy", PASSWORD)).isNotBlank();
    }

    @Test void simpleUsersQueryFiltersAndRequiresAdmin() throws Exception {
        Account a = account("alice", false); Account admin = account("admin", true);
        jdbc.update("UPDATE users SET active=false WHERE id_user=?", a.getId());
        Result result = call("GET", "/api/users/consulta-simple?active=false", admin.getToken(), null);
        assertThat(result.getStatus()).as(result.getText()).isEqualTo(200);
        assertThat(result.getBody().size()).isEqualTo(1);
        assertThat(result.getBody().get(0).get("idUser").asLong()).isEqualTo(a.getId());
        assertThat(result.getText()).doesNotContain("password", "roles");
        Account b = account("bob", false);
        assertThat(call("GET", "/api/users/consulta-simple?active=true", b.getToken(), null).getStatus()).isEqualTo(403);
        assertThat(call("GET", "/api/users/consulta-simple?active=invalid", admin.getToken(), null).getStatus()).isEqualTo(400);
    }

    @Test void nativeUsersQueryCountsProfilesAndIncludesZero() throws Exception {
        Account a = account("alice", false); Account admin = account("admin", true);
        createProfile(a); createProfile(a);
        Result result = call("GET", "/api/users/consulta-nativa", admin.getToken(), null);
        assertThat(result.getStatus()).as(result.getText()).isEqualTo(200);
        assertThat(result.getBody().size()).isEqualTo(2);
        for (JsonNode row : result.getBody()) {
            assertThat(row.get("totalProfiles").asLong()).isEqualTo(row.get("idUser").asLong() == a.getId() ? 2L : 0L);
        }
        assertThat(call("GET", "/api/users/consulta-nativa", a.getToken(), null).getStatus()).isEqualTo(403);
    }

    @Test void simpleProfilesQueryUsesBloodTypeAndNeverReturnsForeignProfiles() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        long own = createProfile(a); createProfile(b);
        var different = profile(); different.put("bloodType", "A-");
        assertThat(call("POST", "/api/healthprofiles", a.getToken(), different).getStatus()).isEqualTo(201);
        Result result = call("GET", "/api/healthprofiles/consulta-simple?bloodType=O%2B", a.getToken(), null);
        assertThat(result.getStatus()).as(result.getText()).isEqualTo(200);
        assertThat(result.getBody().size()).isEqualTo(1);
        assertThat(result.getBody().get(0).get("idHealthProfile").asLong()).isEqualTo(own);
        assertThat(call("GET", "/api/healthprofiles/consulta-simple?bloodType=X", a.getToken(), null).getStatus()).isEqualTo(400);
        Result empty = call("GET", "/api/healthprofiles/consulta-simple?bloodType=AB-", a.getToken(), null);
        assertThat(empty.getStatus()).isEqualTo(404);
        assertThat(empty.getBody().get("message").asText()).contains("No tienes perfiles");
    }

    @Test void nativeProfilesQueryReturnsOnlyOwnedActiveProfiles() throws Exception {
        Account a = account("alice", false); Account b = account("bob", false);
        long active = createProfile(a); createProfile(b);
        var inactive = profile(); inactive.put("active", false);
        assertThat(call("POST", "/api/healthprofiles", a.getToken(), inactive).getStatus()).isEqualTo(201);
        Result result = call("GET", "/api/healthprofiles/consulta-nativa", a.getToken(), null);
        assertThat(result.getStatus()).as(result.getText()).isEqualTo(200);
        assertThat(result.getBody().size()).isEqualTo(1);
        assertThat(result.getBody().get(0).get("idHealthProfile").asLong()).isEqualTo(active);
        jdbc.update("UPDATE health_profiles SET active=false WHERE id_owner_user=?", a.getId());
        assertThat(call("GET", "/api/healthprofiles/consulta-nativa", a.getToken(), null).getStatus()).isEqualTo(404);
    }

    @Test void securityErrorsHaveUserVisibleJsonIncludingInvalidJwt() throws Exception {
        Result absent = call("GET", "/api/healthprofiles", null, null);
        assertThat(absent.getStatus()).isEqualTo(401);
        assertThat(absent.getBody().get("message").asText()).contains("Authorize");
        assertThat(absent.getBody().get("path").asText()).isEqualTo("/api/healthprofiles");
        Result invalid = call("GET", "/api/users", "not-a-jwt", null);
        assertThat(invalid.getStatus()).isEqualTo(401);
        assertThat(invalid.getBody().get("message").asText()).isNotBlank();
    }

    @Test void validationErrorsTellTheUserWhatToFix() throws Exception {
        var invalid = registration("alice"); invalid.put("email", "invalid");
        Result result = call("POST", "/api/users", null, invalid);
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getBody().get("message").asText()).contains("email", "formato válido");
        Account a = account("alice", false);
        Result missing = call("GET", "/api/healthprofiles/consulta-simple", a.getToken(), null);
        assertThat(missing.getStatus()).isEqualTo(400);
        assertThat(missing.getBody().get("message").asText()).contains("bloodType");
        var date = profile(); date.put("birthDate", "not-a-date");
        Result wrongDate = call("POST", "/api/healthprofiles", a.getToken(), date);
        assertThat(wrongDate.getStatus()).isEqualTo(400);
        assertThat(wrongDate.getBody().get("message").asText()).contains("AAAA-MM-DD");
    }

    @Test void registrationAliasKeepsTheCanonicalLocation() throws Exception {
        Result result = call("POST", "/api/users/registro", null, registration("alice"));
        assertThat(result.getStatus()).as(result.getText()).isEqualTo(201);
        assertThat(result.getLocation()).endsWith("/api/users/" + result.getBody().get("idUser").asLong());
        assertThat(login("alice", PASSWORD)).isNotBlank();
    }

    @Test void ordinaryUsersCannotGrantThemselvesAdminRoles() throws Exception {
        Account a = account("alice", false);
        Result result = call("POST", "/api/roles", a.getToken(), Map.of("idUser", a.getId(), "rol", "ROLE_ADMIN"));
        assertThat(result.getStatus()).as(result.getText()).isEqualTo(403);
        assertThat(users.findById(a.getId()).orElseThrow().getRoles()).extracting(Role::getRol).containsExactly("ROLE_USER");
    }

    @Test void swaggerDocumentsAllQueriesAndSafeRequestDtos() throws Exception {
        Result result = call("GET", "/v3/api-docs", null, null);
        assertThat(result.getStatus()).as(result.getText()).isEqualTo(200);
        JsonNode paths = result.getBody().get("paths");
        assertThat(paths.has("/api/users/consulta-simple")).isTrue();
        assertThat(paths.has("/api/users/consulta-nativa")).isTrue();
        assertThat(paths.has("/api/healthprofiles/consulta-simple")).isTrue();
        assertThat(paths.has("/api/healthprofiles/consulta-nativa")).isTrue();
        JsonNode properties = result.getBody().get("components").get("schemas").get("UsersRequestDTO").get("properties");
        assertThat(properties.has("password")).isTrue();
        assertThat(properties.has("passwordHash")).isFalse();
        assertThat(properties.has("idUser")).isFalse();
    }
}
