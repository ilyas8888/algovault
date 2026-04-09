# AlgoVault — Session Notes
> Phase 1 · 2026-04-01

---

## Objectif du projet

Construire **AlgoVault** — une API REST backend pour une plateforme de challenges algorithmiques.
Objectif réel : monter en compétences sur Java, Spring Boot, Docker, Linux, OOP, DSA.

### Roadmap complète

| Phase | Contenu |
|-------|---------|
| **1** | Spring Boot setup, REST basics, premier CRUD ✅ |
| **2** | PostgreSQL + JPA/Hibernate + Flyway migrations |
| **3** | Spring Security + JWT |
| **4** | Services DSA exposés via REST |
| **5** | Docker + docker-compose + Linux |
| **6** | Redis, pagination, Swagger, tests |

---

## Concepts appris cette session

### Le modèle mental du Web

```
TOI (navigateur/app)  →  HTTP  →  Serveur Spring Boot
     "je veux /challenges"          "voilà la liste en JSON"
```

- **Client** = celui qui demande
- **Serveur** = celui qui répond
- **HTTP** = le langage de communication (juste du texte formaté)
- **REST** = conventions pour bien nommer ses URLs et utiliser HTTP correctement

### Méthodes HTTP

| Méthode | Signification |
|---------|---------------|
| `GET`    | Récupérer des données |
| `POST`   | Créer une ressource |
| `PUT`    | Mettre à jour une ressource |
| `DELETE` | Supprimer une ressource |

### Codes de statut HTTP

| Code | Signification |
|------|---------------|
| `200` | OK |
| `201` | Créé avec succès |
| `400` | Données invalides envoyées |
| `401` | Non authentifié |
| `403` | Authentifié mais non autorisé |
| `404` | Ressource introuvable |
| `500` | Erreur serveur |

### JSON

Format utilisé pour échanger des données entre client et serveur.
Spring Boot convertit automatiquement tes objets Java en JSON.

```json
{
  "id": 1,
  "title": "Two Sum",
  "difficulty": "EASY",
  "category": "Arrays"
}
```

### Annotations Spring Boot clés

| Annotation | Rôle |
|------------|------|
| `@SpringBootApplication` | Point d'entrée, active le scan de composants |
| `@RestController` | Cette classe gère des requêtes HTTP et retourne des données |
| `@RequestMapping("/path")` | Préfixe d'URL pour tous les endpoints de la classe |
| `@GetMapping` | Écoute les requêtes `GET` |
| `@GetMapping("/{id}")` | Écoute `GET` avec un paramètre dynamique dans l'URL |
| `@PathVariable` | Capture le `{id}` de l'URL dans une variable Java |
| `@Service` | Marque une classe comme service — Spring la gère et l'injecte |

### Injection de dépendances

Tu n'écris jamais `new ChallengeService()` toi-même.
Spring crée l'instance et l'injecte via le constructeur :

```java
public ChallengeController(ChallengeService service) {
    this.service = service;   // Spring fournit ceci automatiquement
}
```

---

## Ce qu'on a construit

### Structure du projet

```
src/main/java/com/algovault/
├── AlgovaultApplication.java       ← point d'entrée, démarre Tomcat
└── challenge/
    ├── Challenge.java              ← modèle (forme de la donnée)
    ├── ChallengeService.java       ← logique métier (données en dur pour l'instant)
    └── ChallengeController.java    ← routes HTTP
```

### Endpoints disponibles

| Requête | Réponse |
|---------|---------|
| `GET /challenges` | Liste des 5 challenges en JSON |
| `GET /challenges/3` | Challenge #3 en JSON |
| `GET /challenges/99` | 404 Not Found |

---

## Bugs rencontrés et résolus

### Bug 1 — `release version 5 not supported`

**Cause :** IntelliJ n'avait pas importé le projet comme projet Maven.
Il utilisait ses propres paramètres par défaut (Java 1.5).

**Fix :**
- Clic droit sur `pom.xml` → **Add as Maven Project**
- Recharger Maven (bouton ↺)
- Ne pas marquer manuellement `src/main/java` comme Source Root — Maven le fait tout seul

### Bug 2 — Spring Boot version 4.0.5

**Cause :** start.spring.io a généré Spring Boot 4.x qui requiert Java 21+.
Tu as Java 17.

**Fix dans `pom.xml` :**
```xml
<!-- Avant -->
<version>4.0.5</version>

<!-- Après -->
<version>3.2.5</version>
```

Et les dépendances corrigées :
```xml
<!-- Avant (noms incorrects) -->
<artifactId>spring-boot-starter-webmvc</artifactId>
<artifactId>spring-boot-starter-webmvc-test</artifactId>

<!-- Après (noms corrects) -->
<artifactId>spring-boot-starter-web</artifactId>
<artifactId>spring-boot-starter-test</artifactId>
```

### Bug 3 — 404 sur `/challenges` malgré le controller créé

**Cause :** La classe main était dans `com.algovault.algovault` (double algovault).
`@SpringBootApplication` scanne uniquement son package et ses sous-packages.
Le controller dans `com.algovault.challenge` était invisible pour Spring.

```
com.algovault.algovault   ← scan démarre ici
com.algovault.challenge   ← jamais scanné = controller ignoré
```

**Fix :** Déplacer `AlgovaultApplication.java` dans `com.algovault` (le package parent).

```
com.algovault             ← scan démarre ici
com.algovault.challenge   ← sous-package → scanné automatiquement ✅
```

---

## Environnement

| Outil | Version |
|-------|---------|
| OS | Windows 11 |
| Java | OpenJDK 17.0.12 |
| Maven | 3.9.11 |
| Spring Boot | 3.2.5 |
| IDE | IntelliJ IDEA (Community) |

---

## Ce qu'on a appris — Phase 1 Steps 4 & 5

### Recevoir des données — `@RequestBody`

Spring utilise **Jackson** pour convertir le JSON reçu en objet Java.
Jackson a besoin de :
- Un **constructeur vide** `Challenge()`
- Des **setters** pour chaque champ

```java
@PostMapping
public ResponseEntity<Challenge> create(@RequestBody Challenge challenge) {
    Challenge created = service.create(challenge);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);  // 201
}
```

### Nouvelles annotations

| Annotation | Rôle |
|------------|------|
| `@PostMapping` | Écoute les requêtes `POST` |
| `@PutMapping("/{id}")` | Écoute les requêtes `PUT` avec id |
| `@DeleteMapping("/{id}")` | Écoute les requêtes `DELETE` avec id |
| `@RequestBody` | Convertit le body JSON en objet Java |

### Deux paramètres dans un endpoint

Quand on a besoin de l'id (URL) ET d'un body JSON, on utilise les deux :

```java
public ResponseEntity<Challenge> update(@PathVariable Long id, @RequestBody Challenge challenge)
```

### `ArrayList` vs `List.of`

| | `List.of` | `new ArrayList<>(List.of(...))` |
|---|---|---|
| Modifiable | ❌ | ✅ |
| Usage | Données figées | Données dynamiques |

### `ResponseEntity`

Utiliser quand on a plusieurs réponses possibles ou un code HTTP précis :

```java
ResponseEntity.ok(challenge)               // 200
ResponseEntity.notFound().build()          // 404
ResponseEntity.status(HttpStatus.CREATED)  // 201
```

### Single Responsibility Principle

```
Controller  →  reçoit la requête, délègue, retourne la réponse
Service     →  logique métier
Model       →  porte la donnée
```

---

## Endpoints disponibles — CRUD complet ✅

| Méthode | URL | Description |
|---------|-----|-------------|
| `GET` | `/challenges` | Liste tous les challenges |
| `GET` | `/challenges/{id}` | Retourne un challenge |
| `POST` | `/challenges` | Crée un challenge |
| `PUT` | `/challenges/{id}` | Modifie un challenge |
| `DELETE` | `/challenges/{id}` | Supprime un challenge |

---

---

# Phase 2 · 2026-04-02

## Objectif

Remplacer l'ArrayList en mémoire par **PostgreSQL + JPA/Hibernate + Flyway**.
Les données survivent maintenant aux redémarrages du serveur.

---

## Dépendances ajoutées

| Dépendance | Rôle |
|------------|------|
| `spring-boot-starter-data-jpa` | Parler à la BDD depuis Java sans écrire de SQL |
| `postgresql` | Driver de connexion entre Java et PostgreSQL |
| `flyway-core` | Gérer l'historique des migrations SQL |

---

## Concepts appris

### JPA / Hibernate

JPA = Java Persistence API. Norme Java pour interagir avec une BDD.
Hibernate = l'implémentation utilisée par Spring Boot.

### Annotations JPA

| Annotation | Rôle |
|------------|------|
| `@Entity` | Cette classe correspond à une table en BDD |
| `@Table(name="challenges")` | Nomme la table |
| `@Id` | Clé primaire |
| `@GeneratedValue(strategy = GenerationType.IDENTITY)` | Id auto-incrémenté par la BDD |

### `ddl-auto=validate`

Au démarrage, Hibernate vérifie que les entités Java correspondent aux tables en BDD.
Ne modifie rien — c'est Flyway qui gère les changements.

### Optional

`repository.findById(id)` retourne un `Optional<Challenge>`.
C'est un conteneur : il y a peut-être un résultat, peut-être pas.
`.orElse(null)` = si rien trouvé, retourne null.

---

## Le pattern Repository

```java
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
```

Zéro code à écrire. Spring génère automatiquement :

| Méthode | SQL généré |
|---------|-----------|
| `findAll()` | `SELECT * FROM challenges` |
| `findById(id)` | `SELECT * FROM challenges WHERE id = ?` |
| `save(challenge)` | `INSERT` ou `UPDATE` selon si l'id existe |
| `deleteById(id)` | `DELETE FROM challenges WHERE id = ?` |

---

## Flyway — conventions

Les scripts sont dans `src/main/resources/db/migration/`.
Nommage obligatoire : `V{numéro}__{description}.sql`

**Règle d'or : ne jamais modifier un script existant.**
Chaque changement = un nouveau fichier avec le numéro suivant.

| Situation | Fichier à créer |
|-----------|----------------|
| Nouvelle table | `V2__create_xxx_table.sql` |
| Ajouter une colonne | `V2__add_xxx_to_challenges.sql` |
| Modifier un type | `V2__alter_xxx_column.sql` |

Différence avec .NET Entity Framework : en Java/Flyway, les migrations sont écrites manuellement. Plus de contrôle, zéro surprise en production.

---

## Structure du projet après Phase 2

```
src/main/java/com/algovault/
├── AlgovaultApplication.java
└── challenge/
    ├── Challenge.java            ← entité JPA (@Entity)
    ├── ChallengeRepository.java  ← accès BDD (interface JpaRepository)
    ├── ChallengeService.java     ← logique métier (utilise le repository)
    └── ChallengeController.java  ← routes HTTP (inchangé)

src/main/resources/
├── application.properties
└── db/migration/
    └── V1__create_challenges_table.sql
```

---

## Prochaine étape — Phase 3

**Spring Security + JWT.**
Protéger les endpoints avec une authentification par token.

---

# Phase 3 · 2026-04-02

## Objectif

Protéger les endpoints avec **Spring Security + JWT**.
Seuls les utilisateurs authentifiés peuvent accéder aux ressources.

---

## Dépendances ajoutées

| Dépendance | Rôle |
|------------|------|
| `spring-boot-starter-security` | Framework de sécurité Spring (BCrypt inclus) |
| `jjwt-api` | API pour créer et valider des tokens JWT |
| `jjwt-impl` | Implémentation interne de jjwt (runtime) |
| `jjwt-jackson` | Sérialisation JSON du payload JWT (runtime) |

---

## Concepts appris

### BCrypt

On ne stocke jamais un mot de passe en clair en BDD.

```
"monMotDePasse"  →  encode()   →  "$2a$10$xK9z3..."  (stocké en BDD)
"monMotDePasse"  →  matches()  →  comparaison avec le hash  →  true/false
```

Irréversible — impossible de retrouver le mot de passe original.

### Structure d'un token JWT

```
header.payload.signature
```

| Partie | Contenu | Visible ? |
|--------|---------|-----------|
| Header | algorithme | Oui (Base64) |
| Payload | email, expiration | Oui (Base64) |
| Signature | preuve d'authenticité | Oui, mais invérifiable sans la clé secrète |

La clé secrète reste sur le serveur — c'est elle qui rend le token infalsifiable.

### STATELESS

Avec JWT, le serveur ne garde rien en mémoire.
Chaque requête est indépendante — le token suffit à identifier l'utilisateur.

### La chaîne de filtres

```
Requête  →  JwtFilter  →  vérifie le token  →  authentifie  →  Controller
```

`JwtFilter` s'exécute à chaque requête avant le controller.

### JWT et changement d'email

Le token est stateless — le serveur ne peut pas l'invalider.
Si l'email change, l'ancien token reste valide jusqu'à expiration.
Solutions : expiration courte, ou blacklist de tokens en BDD.

---

## Flux complet

```
POST /auth/register  →  BCrypt hache le mot de passe  →  User sauvegardé en BDD
POST /auth/login     →  mot de passe vérifié  →  token JWT retourné
GET  /challenges     →  JwtFilter valide le token  →  200 OK
GET  /challenges     →  sans token  →  403 Forbidden
```

---

## Règles de sécurité (SecurityConfig)

```java
.requestMatchers("/auth/**").permitAll()  // register + login = publics
.anyRequest().authenticated()             // tout le reste = token obligatoire
```

---

## Structure du projet après Phase 3

```
src/main/java/com/algovault/
├── AlgovaultApplication.java
├── auth/
│   └── AuthController.java       ← /auth/register + /auth/login
├── challenge/
│   ├── Challenge.java
│   ├── ChallengeRepository.java
│   ├── ChallengeService.java
│   └── ChallengeController.java
├── security/
│   ├── JwtService.java           ← générer + valider les tokens
│   ├── JwtFilter.java            ← intercepte chaque requête
│   └── SecurityConfig.java       ← règles d'accès
└── user/
    ├── User.java
    └── UserRepository.java

src/main/resources/db/migration/
├── V1__create_challenges_table.sql
└── V2__create_users_table.sql
```

---

## Prochaine étape — Phase 4

**Services DSA exposés via REST.**
Implémenter des algorithmes (tri, recherche, graphes...) et les exposer comme endpoints.

---

# Phase 4 · 2026-04-02

## Objectif

Implémenter des algorithmes DSA et les exposer comme endpoints REST sécurisés par JWT.

---

## Concepts appris

### Recherche linéaire

Parcourt chaque élément un par un jusqu'à trouver la cible.
Complexité : **O(n)**. Fonctionne sur tout tableau (trié ou non).

```
[1, 5, 3, 7, 8, 9]  target=9
→ vérifie 1, 5, 3, 7, 8, 9 → trouvé à l'index 5
```

### Recherche binaire

Divise le tableau en deux à chaque étape.
Complexité : **O(log n)**. Exige un tableau **trié**.

```
[1, 3, 5, 7, 8, 9]  target=7
→ mid=5 → 7>5 → cherche à droite
→ mid=8 → 7<8 → cherche à gauche
→ mid=7 → trouvé à l'index 3
```

### Bubble Sort

Compare les paires adjacentes et échange si désordre. Répète jusqu'à ce que le tableau soit trié.
Complexité : **O(n²)**.

```
[5, 3, 1, 4]
passage 1 → [3, 1, 4, 5]
passage 2 → [1, 3, 4, 5]  ✅
```

### Jackson et le constructeur vide

Jackson (convertisseur JSON → Java) a besoin d'un **constructeur vide** pour créer l'objet avant de remplir les champs.
Si seulement un constructeur paramétré existe → erreur de désérialisation.

```java
public SortRequest() {}   // obligatoire pour Jackson
```

### Convention Java — camelCase

Les méthodes commencent toujours par une **minuscule** :
```java
bubbleSort()   // ✅
BubbleSort()   // ❌
```

---

## Bugs rencontrés et résolus

### Bug 1 — `array.length` sans `-1` dans binarySearch

`max = array.length` → index hors limites possible (`ArrayIndexOutOfBoundsException`).
**Fix :** `max = array.length - 1`

### Bug 2 — Boucle infinie dans binarySearch

`max` et `min` ne convergeaient pas correctement → la condition `while` restait vraie pour toujours → Postman bloqué sans réponse.
**Fix :** utiliser `left`/`right` qui se croisent proprement avec `left = mid + 1` et `right = mid - 1`.

### Bug 3 — Constructeur paramétré sans constructeur vide

Jackson ne pouvait pas instancier `SortRequest` → erreur 400.
**Fix :** ajouter `public SortRequest() {}`

---

## Endpoints disponibles — Phase 4

| Méthode | URL | Description |
|---------|-----|-------------|
| `POST` | `/dsa/search/linear` | Recherche linéaire dans un tableau |
| `POST` | `/dsa/search/binary` | Recherche binaire dans un tableau trié |
| `POST` | `/dsa/sort/bubble` | Tri par bubble sort |

Tous ces endpoints exigent un token JWT dans le header `Authorization: Bearer <token>`.

---

## Structure du projet après Phase 4

```
src/main/java/com/algovault/
├── AlgovaultApplication.java
├── auth/
│   └── AuthController.java
├── challenge/
│   ├── Challenge.java
│   ├── ChallengeRepository.java
│   ├── ChallengeService.java
│   └── ChallengeController.java
├── dsa/
│   ├── SearchRequest.java
│   ├── SearchService.java
│   ├── SearchController.java
│   ├── SortRequest.java
│   ├── SortService.java
│   └── SortController.java
├── security/
│   ├── JwtService.java
│   ├── JwtFilter.java
│   └── SecurityConfig.java
└── user/
    ├── User.java
    └── UserRepository.java
```

---

## Prochaine étape — Phase 5

**Docker + docker-compose + Linux.**
Containeriser l'application et lancer PostgreSQL + Spring Boot ensemble.

---

# Phase 5 · 2026-04-02

## Objectif

Containeriser l'application Spring Boot et orchestrer PostgreSQL + app avec docker-compose.

---

## Concepts appris

### Image vs Container

| Terme | Analogie |
|-------|---------|
| **Image** | La recette — instructions pour construire l'environnement |
| **Container** | Le plat cuisiné — l'image en train de tourner |
| **Dockerfile** | Le fichier qui décrit comment construire l'image |
| **docker-compose** | Lance plusieurs containers ensemble |

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre        # part d'une image Java 17
WORKDIR /app                        # dossier de travail dans le container
COPY target/algovault-0.0.1-SNAPSHOT.jar app.jar  # copie le .jar
ENTRYPOINT ["java", "-jar", "app.jar"]             # commande au démarrage
```

### Communication entre containers

Dans Docker, les containers ne se parlent pas via `localhost` mais via le **nom de service** défini dans `docker-compose.yml`.

```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/algovault
#                                         ↑
#                               nom du service, pas localhost
```

### Variables d'environnement

Les variables dans `docker-compose.yml` écrasent `application.properties` au démarrage.

---

## Commandes essentielles

| Commande | Rôle |
|----------|------|
| `./mvnw package -DskipTests` | Compile l'app en `.jar` |
| `docker compose up --build` | Construit l'image et lance les containers |
| `docker compose down` | Arrête les containers |
| `docker compose down -v` | Arrête et supprime les volumes (données BDD) |

---

## Bugs rencontrés et résolus

### Bug 1 — Indentation dans le Dockerfile

Docker n'accepte pas l'indentation — chaque instruction doit commencer en début de ligne.
**Fix :** supprimer les espaces en début de ligne.

### Bug 2 — Password authentication failed

Le container PostgreSQL avait été créé avec un mot de passe différent de celui dans `docker-compose.yml`.
**Fix :** aligner le mot de passe dans `docker-compose.yml` avec celui de `application.properties`.

---

## Structure des fichiers Docker

```
algovault/
├── Dockerfile
├── docker-compose.yml
└── src/...
```

---

## Prochaine étape — Phase 6

**Redis, pagination, Swagger, tests.**

---

# Phase 6 · 2026-04-02

## Objectif

Ajouter pagination, Swagger et Redis cache à l'API.

---

## Pagination

Spring Data JPA gère la pagination automatiquement via `JpaRepository` qui hérite de `PagingAndSortingRepository`.

```java
// Service
@Cacheable("challenges")
public Page<Challenge> findAll(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return repository.findAll(pageable);
}

// Controller
@GetMapping
public Page<Challenge> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return service.findAll(page, size);
}
```

Appel : `GET /challenges?page=0&size=3`

Spring retourne automatiquement les métadonnées : `totalPages`, `totalElements`, `first`, `last`, etc.

---

## Swagger

Dépendance : `springdoc-openapi-starter-webmvc-ui:2.3.0`

URL : `http://localhost:8080/swagger-ui/index.html`

Les URLs Swagger doivent être autorisées dans `SecurityConfig` :
```java
.requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
```

Pour utiliser JWT dans Swagger, créer un `SwaggerConfig.java` qui configure un `SecurityScheme` de type HTTP bearer. Le bouton **Authorize** apparaît en haut — coller le token une fois pour tous les endpoints.

---

## Redis Cache

Dépendance : `spring-boot-starter-data-redis`

Activer dans `AlgovaultApplication.java` : `@EnableCaching`

Ajouter dans `docker-compose.yml` :
```yaml
redis:
  image: redis:7
  ports:
    - "6379:6379"
```

Variable d'environnement dans le service `app` :
```yaml
SPRING_DATA_REDIS_HOST: redis
```

### Configuration — RedisConfig.java

Par défaut Spring sérialise en binaire JDK (illisible). On configure JSON + TTL :

```java
@Bean
public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                )
            );
}
```

### Annotations

| Annotation | Rôle |
|------------|------|
| `@EnableCaching` | Active le système de cache (sur la classe principale) |
| `@Cacheable("nom")` | Retourne depuis le cache si présent, sinon exécute + stocke |
| `@Cacheable(value="nom", key="#id")` | Cache avec une clé dynamique (ex: `challenge::3`) |
| `@CacheEvict(value={"a","b"}, allEntries=true)` | Vide le cache lors d'une modification |

### Flux de fonctionnement

```
1er appel  → Redis vide → PostgreSQL → stocke dans Redis → retourne
2ème appel → Redis plein → retourne directement (aucun SQL)
Modification → @CacheEvict → Redis vidé
Prochain appel → Redis vide → PostgreSQL → re-stocke
```

Preuve dans les logs : le 2ème appel identique ne génère **aucune ligne Hibernate** dans la console.

### Pièges rencontrés

**1 — `Page<Challenge>` non sérialisable**
`Page` est une interface, Jackson ne sait pas la reconstruire depuis Redis.
→ Solution : cacher une `List<Challenge>`, reconstruire le `Page` en mémoire avec `PageImpl`.

```java
@Cacheable("challenges")
public List<Challenge> findAllCached() {
    return repository.findAll();
}

public Page<Challenge> findAll(int page, int size) {
    List<Challenge> all = self.findAllCached();
    Pageable pageable = PageRequest.of(page, size);
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), all.size());
    return new PageImpl<>(all.subList(start, end), pageable, all.size());
}
```

**2 — Self-invocation (problème AOP)**
`this.findAllCached()` depuis le même bean bypass le proxy Spring → `@Cacheable` ignoré.
→ Solution : s'injecter soi-même avec `@Lazy` et utiliser `self.findAllCached()`.

```java
@Autowired
@Lazy
private ChallengeService self;
```

---

## Tests

### 3 types de tests

| Type | Annotation | Ce qui tourne |
|------|-----------|---------------|
| Unitaire | `@ExtendWith(MockitoExtension.class)` | Logique pure, sans Spring |
| Couche web | `@WebMvcTest` | Controllers + Security, sans BDD |
| Intégration | `@SpringBootTest` + H2 | Application complète |

### Tests unitaires — `ChallengeServiceTest`

Mockito crée de faux beans sans démarrer Spring.

```java
@Mock
ChallengeRepository repository;   // faux repository

@InjectMocks
ChallengeService service;          // vrai service avec le faux repository

when(repository.findById(1L)).thenReturn(Optional.of(challenge));  // programmer le comportement
verify(repository, times(1)).findById(1L);                          // vérifier l'appel
```

### Tests couche web — `ChallengeControllerTest`

`@WebMvcTest` charge uniquement controllers + security, pas la BDD.

```java
@MockBean ChallengeService service;   // remplace le vrai service
@MockBean JwtService jwtService;      // requis par JwtFilter

@WithMockUser                         // simule un utilisateur connecté sans JWT
mockMvc.perform(get("/challenges/1"))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.title").value("Two Sum"));

// POST et DELETE nécessitent .with(csrf())
mockMvc.perform(post("/challenges").with(csrf())...);
```

### Tests d'intégration — `ChallengeIntegrationTest`

Application complète avec H2 à la place de PostgreSQL.

Fichier `src/test/resources/application-test.properties` :
```properties
spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE
spring.cache.type=none
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
```

- `MODE=PostgreSQL` → H2 comprend `BIGSERIAL` (syntaxe PostgreSQL)
- `spring.cache.type=none` → Redis désactivé pendant les tests
- `@ActiveProfiles("test")` → charge `application-test.properties`
- `@AutoConfigureMockMvc` → injecte MockMvc dans un `@SpringBootTest`

---

## Statut Phase 6

| Sujet | Statut |
|-------|--------|
| Pagination | ✅ |
| Swagger | ✅ |
| Redis | ✅ |
| Tests | ✅ |

---

## Prochaine étape — Phase 7

**Nouveaux algorithmes DSA** — Merge Sort, DFS, BFS.

---

# Phase 7 · 2026-04-03

## Objectif

Implémenter des algorithmes DSA avancés : Merge Sort, DFS, BFS.

---

## Merge Sort

### Concept

Algorithme de tri **diviser pour régner**. Complexité : **O(n log n)**.

```
[5, 3, 8, 1]
→ divise → [5, 3] | [8, 1]
→ divise → [5] [3] | [8] [1]   ← condition d'arrêt (1 élément)
→ fusionne → [3, 5] | [1, 8]
→ fusionne → [1, 3, 5, 8] ✅
```

- **Descente** : divise récursivement jusqu'aux tableaux à 1 élément
- **Remontée** : fusionne en comparant les éléments deux à deux

### Comparaison

| Algorithme | Complexité | Quand utiliser |
|-----------|-----------|----------------|
| Bubble Sort | O(n²) | Jamais en prod |
| Merge Sort | O(n log n) | Tri général |

Pour n=1000 : Bubble Sort = 1 000 000 ops, Merge Sort = 10 000 ops.

### Endpoint

`POST /dsa/sort/merge` — même `SortRequest` que bubble sort.

---

## DFS (Depth-First Search — Parcours en profondeur)

### Concept

Algorithme de **parcours de graphe** qui plonge le plus loin possible avant de revenir en arrière. Utilise la **récursion** (pile d'appels implicite).

```
Graphe : 1 → [2, 3, 7], 2 → [4], 3 → [4, 5], 4 → [1], 5 → []
DFS depuis 1 : [1, 2, 4, 3, 5, 7]
→ on finit une branche entière avant de passer à la suivante
```

- `Set<Integer> visited` → évite les boucles infinies (cycles)
- `graph.getOrDefault(node, emptyList())` → nœud sans voisins définis = liste vide

### Endpoint

`POST /dsa/graph/dfs` — body : `GraphRequest` (`Map<Integer, List<Integer>>` + `startNode`)

---

## BFS (Breadth-First Search — Parcours en largeur)

### Concept

Algorithme de **parcours de graphe** qui explore niveau par niveau. Utilise une **Queue** (file FIFO).

```
Graphe : 1 → [2, 3, 7], 2 → [4], 3 → [4, 5]
BFS depuis 1 : [1, 2, 3, 7, 4, 5]
→ tous les voisins directs d'abord, puis leurs voisins
```

### Pourquoi BFS ?

| Utilise DFS quand... | Utilise BFS quand... |
|----------------------|----------------------|
| Explorer toutes les possibilités | Trouver le chemin le plus court |
| Labyrinthe, arbres de décision | GPS, réseaux sociaux (LinkedIn) |
| La distance ne compte pas | La distance compte |

BFS garantit le **chemin le plus court** (en nombre d'étapes). DFS ne le garantit pas.

### Endpoint

`POST /dsa/graph/bfs` — même `GraphRequest` que DFS.

---

## Dijkstra — Chemin le plus court avec poids

### Concept

Trouve le chemin le plus court dans un graphe **pondéré** (arêtes avec des coûts différents). BFS ne suffit pas quand les distances varient.

```
1 --1--> 2 --1--> 4   (coût total : 2)
1 --5--> 3 --1--> 4   (coût total : 6)
→ Dijkstra choisit 1→2→4 ✅
```

### Structure du graphe

`Map<Integer, Map<Integer, Integer>>` : nœud → (voisin → poids)

Utilise une **PriorityQueue** qui traite toujours le nœud le moins coûteux en premier.

```
distances = {1:0, 2:∞, 3:∞, 4:∞}
→ traite 1 → met à jour 2=1, 3=5, 5=7
→ traite 2 → met à jour 4=2 (1+1 < ∞)
→ traite 3 → 4 déjà à 2, 6>2 → pas de mise à jour
→ résultat : {1:0, 2:1, 3:5, 4:2, 5:7}
```

### Cas concrets

- **GPS** : trajet le plus rapide (routes ont des durées différentes)
- **Réseau** : envoyer données par le chemin le moins coûteux
- **Jeux vidéo** : personnage cherche le chemin optimal

### Endpoint

`POST /dsa/graph/dijkstra` — body : `DijkstraRequest` (`Map<Integer, Map<Integer, Integer>>` + `startNode`)

---

## Structure du projet après Phase 7

```
src/main/java/com/algovault/dsa/
├── SearchRequest.java
├── SearchService.java
├── SearchController.java
├── SortRequest.java
├── SortService.java
├── SortController.java
├── GraphRequest.java       ← DFS + BFS
├── GraphService.java       ← dfs(), bfs(), dijkstra()
├── GraphController.java    ← /dsa/graph/dfs, /bfs, /dijkstra
└── DijkstraRequest.java    ← graphe pondéré
```

## Tous les endpoints DSA

| Méthode | URL | Algorithme |
|---------|-----|-----------|
| `POST` | `/dsa/search/linear` | Recherche linéaire O(n) |
| `POST` | `/dsa/search/binary` | Recherche binaire O(log n) |
| `POST` | `/dsa/sort/bubble` | Bubble Sort O(n²) |
| `POST` | `/dsa/sort/merge` | Merge Sort O(n log n) |
| `POST` | `/dsa/graph/dfs` | Parcours en profondeur |
| `POST` | `/dsa/graph/bfs` | Parcours en largeur |
| `POST` | `/dsa/graph/dijkstra` | Chemin le plus court (pondéré) |
