## Présentation du projet

Ce projet consiste à étendre une application basée sur une architecture microservices afin d’y intégrer
une persistance des données avec MySQL, une communication asynchrone à l’aide de Kafka,
ainsi qu’un service de notification découplé.

L’objectif principal est de respecter le principe *Database per Service* et de déployer l’ensemble
de l’application à l’aide de Docker Compose.

## Architecture générale

L’application est composée des microservices suivants :

- **Eureka Server** : découverte des services
- **Gateway** : point d’entrée unique pour l’application
- **User Service** : gestion des utilisateurs
- **Book Service** : gestion des livres
- **Emprunt Service** : gestion des emprunts et production des événements Kafka
- **Notification Service** : consommation des événements Kafka et gestion des notifications

Chaque service métier dispose de sa propre base de données MySQL.

---

## Bases de données

| Service | Base de données |
|------|----------------|
| User Service | db_user |
| Book Service | db_book |
| Emprunt Service | db_emprunter |

Les bases de données sont lancées via Docker et les tables sont générées automatiquement par JPA.

---

## Communication asynchrone avec Kafka

Lors de la création d’un emprunt :
- le **emprunt-service** publie un message sur le topic Kafka `emprunt-created`
- le **notification-service** consomme ce message
- une notification est affichée dans les logs du service

Aucun appel REST direct n’est effectué entre ces deux services.

---

## Démarrage de l’application

### Prérequis
- Docker
- Docker Compose

### Lancer le projet
```bash
docker-compose up -d
