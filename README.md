# 🎬 RuffinaryV2 – Gestionnaire de Collection de Films

## 📖 Description du projet

**Ruffinary** est une application Java permettant de **gérer sa collection de films physiques** (LaserDisc, DVD, Blu-ray, 4K, 3D, HD-DVD, etc.).  
Elle offre la possibilité d’ajouter, modifier, supprimer et visualiser facilement ses films, tout en les organisant par **étagères**.

Cette version est une **refonte complète** du projet original, intégrant une architecture **MVC** et plusieurs **design patterns** (*Singleton*, *Composite*) pour un code plus structuré, clair et extensible.

---

## 🚀 Fonctionnalités

### 📚 Gestion de la collection
- ➕ **Ajouter** des étagères et des films  
- ✏️ **Modifier** les informations d’un film  
- ❌ **Supprimer** une étagère ou un film  
- 🔍 **Rechercher** un film via différents critères (titre, genre, format, etc.)

### 👀 Visualisation
- 🧾 **Afficher la liste complète** des films de la collection  
- 🖼️ **Voir les détails** d’un film (avec image si disponible) via un **Manager d’entité**  
- 🗂️ **Visualiser les étagères** et les films qu’elles contiennent via un **Manager d’étagère**

### 🧩 Ajout de films
- 🖊️ **Ajout manuel** d’un film  
- 🌐 **Ajout automatique via API** (recherche par **code-barres**)  
  > Formats pris en charge : LaserDisc, DVD, Blu-ray, Blu-ray 3D, Blu-ray 4K, HD-DVD

### 💾 Gestion des données
- 📤 **Exporter la base de données** en format **CSV**  
- 🗄️ **Connexion à une base locale** installée sur le PC

---

## ⚙️ Fonctionnement technique

Le projet repose sur deux grands axes :

1. **Utilisation d’API externes** pour récupérer automatiquement les informations d’un film (titre, réalisateur, affiche, etc.) via son code-barres.  
2. **Connexion à une base de données locale**, assurant une gestion indépendante et performante des données de collection.

---

## 🎯 Objectif

> Offrir un outil complet et ergonomique pour **gérer sa collection physique de films**, avec la possibilité de savoir **où chaque film se trouve**.

L’objectif de *Ruffinary* est aussi de :
- Faciliter la gestion de collections contenant **de nombreux exemplaires**
- Structurer le code avec une **architecture claire (MVC)**
- Illustrer l’usage de **design patterns** pour une meilleure maintenance et évolutivité

---

## 🧱 Architecture

### 🧩 Modèle MVC
- **Modèle :** gère les entités (Film, Étagère, Base de données)  
- **Vue :** interface graphique 
- **Contrôleur :** assure la logique applicative et la communication entre modèle et vue

### 🧠 Design Patterns utilisés
- **Singleton :** pour la gestion unique des entités 
- **Composite :** pour la hiérarchisation des étagères et des films

