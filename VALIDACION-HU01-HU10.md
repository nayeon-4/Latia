# Validación HU01–HU10

Fecha de ejecución registrada: 2026-10-02T21:45:14-05:00.

**Resultado: BUILD SUCCESS. 26 pruebas, 0 fallos, 0 errores, 0 omitidas.**

La compilación y las pruebas se ejecutaron con JDK 21 y Maven 3.9.16, conservando Spring Boot 4.1.1 del proyecto. Se usó el perfil test con H2 2.4.240 en modo PostgreSQL. Las pruebas de integración levantan Tomcat en un puerto temporal y ejercitan solicitudes HTTP, filtros de seguridad JWT, validaciones, servicios, repositorios y restricciones de persistencia.

Comando reproducible desde la raíz: `mvnw.cmd test` (Windows) o `bash mvnw test` (Linux/macOS). Requiere JDK 21 y descarga de dependencias la primera vez.

## Revisión de estilo solicitada

Se sustituyeron los seis DTO record por clases con campos privados, constructores, getters y setters. Se actualizaron sus consumidores y también los auxiliares de prueba. No quedan declaraciones record en src/main ni src/test. Se repitieron las 26 pruebas después de esta conversión, con resultado correcto.

## Pruebas ejecutadas

| Clase | Caso | Resultado |
|---|---|---|
| Hu01To10IntegrationTests | `hu06MapsHolderAndRejectsSecondPersonalProfile` | Correcto |
| Hu01To10IntegrationTests | `hu10DeletesOwnProfileAndSubsequentGetIs404` | Correcto |
| Hu01To10IntegrationTests | `disabledAccountAndRemovedAdminRoleTakeEffectOnExistingToken` | Correcto |
| Hu01To10IntegrationTests | `rejectsNonPositivePathIds` | Correcto |
| Hu01To10IntegrationTests | `userRoutesRequireAuthenticationExceptRegistration` | Correcto |
| Hu01To10IntegrationTests | `hu10Returns409ForRelatedDataWithoutDeletingEitherRecord` | Correcto |
| Hu01To10IntegrationTests | `hu06RejectsInvalidProfilesAndForeignLinks` | Correcto |
| Hu01To10IntegrationTests | `hu04RejectsForeignChangesInvalidDataAndDuplicates` | Correcto |
| Hu01To10IntegrationTests | `hu04UpdatesByPathIdWithoutChangingPasswordOrRole` | Correcto |
| Hu01To10IntegrationTests | `postWithExistingIdNeverOverwritesExistingAccountOrProfile` | Correcto |
| Hu01To10IntegrationTests | `changingUsernameRequiresFreshLoginAndMissingUpdateIdIs404ForAdmin` | Correcto |
| Hu01To10IntegrationTests | `hu01RejectsInvalidAndDuplicateAccounts` | Correcto |
| Hu01To10IntegrationTests | `hu08FindsOwnProfileButNotForeignOrMissing` | Correcto |
| Hu01To10IntegrationTests | `hu09RejectsOwnerTransferAndForeignUpdates` | Correcto |
| Hu01To10IntegrationTests | `hu02OnlyAdminCanListUsersAndNoHashLeaks` | Correcto |
| Hu01To10IntegrationTests | `hu07Returns404WhenEmptyAndOnlyListsOwnProfiles` | Correcto |
| Hu01To10IntegrationTests | `hu03FindsOwnUserAndReturns404ForMissingIdToAdmin` | Correcto |
| Hu01To10IntegrationTests | `changingPasswordRequiresOldPasswordAndDoesNotAlterOtherAccount` | Correcto |
| Hu01To10IntegrationTests | `hu06CreatesNamedDependentWithOwnerFromJwtAndCorrectMapping` | Correcto |
| Hu01To10IntegrationTests | `hu05BlocksDeletionWithProfilesAndRollsBack` | Correcto |
| Hu01To10IntegrationTests | `hu05DeletesAccountAndReturns404WhenAdminRepeats` | Correcto |
| Hu01To10IntegrationTests | `passwordValidationCountsUtf8BytesAndSupportsLegacyAlias` | Correcto |
| Hu01To10IntegrationTests | `deletedAccountTokenCannotBecomeANewAccountWithSameUsername` | Correcto |
| Hu01To10IntegrationTests | `hu01CreatesUserWithHashRoleAndServerOwnedFields` | Correcto |
| Hu01To10IntegrationTests | `hu09UpdatesProfileUsingPathIdAndPreservesOwnership` | Correcto |
| LatiaLatiApplicationTests | `contextLoads` | Correcto |

## Límites de esta verificación

- No se conectó a la PostgreSQL personal del grupo ni se ensayó su esquema histórico. H2 permite comprobar este CRUD, pero no sustituye probar migraciones y consultas nativas en PostgreSQL.
- No se cerraron ni modificaron tarjetas de Trello. La implementación y esta evidencia deben contrastarse con las decisiones documentadas en HU01-HU10.md.
- Los 25 casos de integración cubren las diez HU, rutas protegidas, acceso entre cuentas, campos internos, validación, conflictos, cambios de contraseña y vigencia de identidad. La prueba restante comprueba carga del contexto.
- Las consultas HU69, el DELETE de ClinicalRecord y los demás módulos de la auditoría quedan fuera de esta entrega. La autorización global de la aplicación aún requiere trabajo.
- Los JWT emitidos antes del cambio carecen del identificador estable userId: volver a iniciar sesión.
