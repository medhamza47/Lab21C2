# Lab21C2 — Architecture microservices avec WebClient

| Module | Port | Rôle |
| --- | ---: | --- |
| `eureka-server` | 8761 | Registre Eureka |
| `service-client` | 8081 | API clients et base `clientservicedb` |
| `service-car` | 8082 | API voitures et base `carservicedb` |

## Lancer le projet

Java 17+, Maven et MySQL sont nécessaires. Vérifier la connexion avec
`mysql -h 127.0.0.1 -P 3306 -u root -p`. Le compte doit pouvoir créer les deux
bases ou disposer de droits sur les bases déjà créées.

La configuration utilise `MYSQL_HOST` (localhost), `MYSQL_PORT` (3306),
`MYSQL_USER` (root) et `MYSQL_PASSWORD` (vide par défaut).
Définir ces variables dans les terminaux des deux services selon la configuration
MySQL locale.

```bash
mvn clean package
```

Lancer les commandes dans trois terminaux, dans cet ordre.

```bash
java -jar eureka-server/target/eureka-server-1.0.0.jar
java -jar service-client/target/service-client-1.0.0.jar
java -jar service-car/target/service-car-1.0.0.jar
```

Ouvrir le tableau de bord sur http://localhost:8761 et attendre l’apparition
de `SERVICE-CLIENT` et `SERVICE-CAR`. Les services annoncent `localhost` par
défaut. En cas de déploiement sur plusieurs hôtes, définir
`EUREKA_INSTANCE_HOSTNAME` pour chaque service avec son nom accessible.

## Scénario de validation

```bash
curl -fsS -X POST http://localhost:8081/api/clients \
  -H 'Content-Type: application/json' -d '{"nom":"Salma","age":22}'
curl -fsS http://localhost:8081/api/clients
```

Remplacer `1` par l’identifiant retourné à la création si nécessaire.

```bash
curl -fsS http://localhost:8082/api/test/client/1
curl -fsS -X POST http://localhost:8082/api/cars \
  -H 'Content-Type: application/json' -d '{"marque":"Toyota","modele":"Yaris","clientId":1}'
curl -fsS http://localhost:8082/api/cars
curl -fsS http://localhost:8082/api/cars/byClient/1
```

Les deux dernières réponses incluent l’objet `client` avec le nom Salma et
l’âge 22. `service-car` persiste la référence `clientId` dans sa base. Il utilise
un WebClient configuré avec `@LoadBalanced` pour résoudre le nom
`SERVICE-CLIENT` via Eureka et enrichir la réponse. Le champ JPA `@Transient`
contient le DTO récupéré. L’appel bloquant a un délai maximum de cinq secondes.

## Dépannage

| Symptôme | Vérification |
| --- | --- |
| Aucune instance de `SERVICE-CLIENT` | Vérifier l’enregistrement Eureka, `defaultZone`, la dépendance LoadBalancer et `@LoadBalanced`. |
| Instance visible mais appel en échec | Appeler directement `/api/clients/{id}` sur le port 8081 et consulter les logs. |
| Échec au démarrage de JPA | Vérifier l’état de MySQL, les identifiants et les droits sur les deux bases. |
| Réponse HTTP 404 | Vérifier le port et le chemin de l’endpoint. |
