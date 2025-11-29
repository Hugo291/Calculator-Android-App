# 🧮 DigitClash - Math Game Challenge

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Material Design](https://img.shields.io/badge/Design-Material-757575?style=for-the-badge&logo=material-design&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange?style=for-the-badge)

*Un jeu de calcul mental addictif pour entraîner votre cerveau ! 🧠⚡*

</div>

---

## 📖 À propos

**DigitClash** est une application Android de calcul mental rapide qui transforme l'apprentissage des mathématiques en un défi ludique et chronométré. Testez vos compétences en addition, soustraction, multiplication et division dans une course contre la montre !

### ✨ Caractéristiques principales

- 🎯 **6 modes de jeu** : Addition, Soustraction, Multiplication, Division, Aléatoire et Tous
- ⏱️ **Système de timer dynamique** : 10 secondes par question avec bonus pour les bonnes réponses
- 📊 **Progression adaptative** : 7 niveaux de difficulté qui évoluent selon vos performances
- 🎨 **Interface moderne** : Design Material coloré avec animations fluides
- 🏆 **Système de score** : Comptage des bonnes et mauvaises réponses en temps réel
- ⏸️ **Pause & Reprise** : Mettez le jeu en pause à tout moment
- 🎊 **Écran de victoire** : Célébrez votre score après 15 rounds réussis

---

## 🎮 Comment jouer

### Règles du jeu

1. **Choisissez votre mode** : Sélectionnez une opération mathématique depuis le menu principal
2. **Résolvez l'opération** : Une équation apparaît avec un chronomètre de 10 secondes
3. **Entrez votre réponse** : Utilisez le clavier numérique pour saisir le résultat
4. **Validez** : Appuyez sur le bouton vert pour soumettre votre réponse

### Système de points

- ✅ **Bonne réponse** : +1 point, chronomètre rechargé, nouvelle équation
- ❌ **Mauvaise réponse** : L'équation reste affichée, vous pouvez réessayer
- ⏰ **Temps écoulé** : Fin de la partie, votre score est affiché

### Niveaux de difficulté

Le jeu s'adapte automatiquement à votre progression :

| Niveau | Seuil | Plage de valeurs | Opérations |
|--------|-------|------------------|------------|
| 1 | 0-9 | 1-15 | +, -, ×, ÷ |
| 2 | 10-19 | 26-50 | +, -, × |
| 3 | 20-34 | 40-100 | +, -, ×, ÷ |
| 4 | 35-59 | 20-120 | +, -, ×, ÷ |
| 5 | 60-79 | 20-150 | +, -, ×, ÷ |
| 6 | 80-99 | 20-200 | +, -, ×, ÷ |
| 7 | 100+ | 20-300 | +, -, ×, ÷ |

---

## 🛠️ Stack technique

### Technologies

- **Langage** : Java 11
- **SDK minimum** : API 24 (Android 7.0)
- **SDK cible** : API 35 (Android 15)
- **Build Tool** : Gradle 9.2.1
- **AGP** : 8.13.1

### Bibliothèques

```gradle
dependencies {
    // AndroidX Core
    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    
    // Material Design Components
    implementation 'com.google.android.material:material:1.10.0'
    
    // Google Mobile Ads
    implementation 'com.google.android.gms:play-services-ads'
}
```

### Architecture

L'application suit une architecture **MVC simplifiée** :

```
app/
├── Activities/          # Contrôleurs (GameActivity, MenuActivity)
├── Models/             # Logique métier (Calculation, Timer)
├── Interfaces/         # Contrats (TimerActions)
├── Utils/              # Classes utilitaires (GameConfig, ButtonAnimationHelper)
└── res/                # Ressources (layouts, drawables, values)
```

---

## 🚀 Installation & Développement

### Prérequis

- Android Studio Ladybug ou supérieur
- JDK 11 ou supérieur
- Android SDK 35

### Étapes d'installation

1. **Clonez le dépôt**
   ```bash
   git clone https://github.com/votre-username/digitclash.git
   cd digitclash
   ```

2. **Ouvrez dans Android Studio**
    - File → Open → Sélectionnez le dossier du projet

3. **Synchronisez Gradle**
    - Android Studio devrait automatiquement synchroniser
    - Ou cliquez sur "Sync Project with Gradle Files"

4. **Lancez l'application**
    - Sélectionnez un émulateur ou connectez un appareil physique
    - Cliquez sur Run (▶️) ou appuyez sur `Shift + F10`

### Build en ligne de commande

```bash
# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease

# Installer sur un appareil
./gradlew installDebug
```

---

## 📁 Structure du projet

### Activités principales

**MenuActivity** (`app/src/main/java/.../Activities/MenuActivity.java`)
- Menu principal avec sélection du mode de jeu
- Animations de boutons avec effet de profondeur 3D
- Navigation vers GameActivity avec paramètre d'opération

**GameActivity** (`app/src/main/java/.../Activities/GameActivity.java`)
- Logique principale du jeu
- Gestion du timer et des rounds
- Validation des réponses et progression

### Modèles

**Calculation** (`app/src/main/java/.../Models/Calculation.java`)
- Génération d'équations mathématiques valides
- Système de difficulté adaptatif
- Validation des opérations (division exacte, résultats positifs)

**Timer** (`app/src/main/java/.../Models/Timer.java`)
- Compte à rebours de 10 secondes
- Gestion de pause/reprise
- Callbacks pour mise à jour UI

### Utilitaires

**GameConfig** (`app/src/main/java/.../Utils/GameConfig.java`)
- Constantes centralisées
- Configuration des niveaux
- Paramètres de jeu

**ButtonAnimationHelper** (`app/src/main/java/.../Utils/ButtonAnimationHelper.java`)
- Animations de pression de boutons
- Réutilisable entre activités
- Effet de profondeur 3D

---

## 🎨 Design & UI/UX

### Palette de couleurs

```xml
<!-- Boutons principaux -->
<color name="button_yellow_strong">#f0c13c</color>    <!-- Addition -->
<color name="button_red_coral">#ee6967</color>        <!-- Soustraction -->
<color name="button_orange_light">#f79883</color>     <!-- Multiplication -->
<color name="button_blue_light">#86d5f4</color>       <!-- Division -->
<color name="button_purple">#d59eea</color>            <!-- Aléatoire -->
<color name="button_green_mint">#4fcaa3</color>        <!-- Tous -->
<color name="button_blue_medium">#5fa6d3</color>       <!-- Chiffres -->

<!-- Interface -->
<color name="text_dark_blue">#2a2c36</color>
<color name="main_background">#F3F3F3</color>
```

### Animations

- **Effet de profondeur 3D** sur tous les boutons
- **Transition fluide** entre les états pressed/released
- **Durées configurables** (100ms press, 300ms release)

### Polices

- **Fredoka SemiBold** : Police principale pour tous les textes
- Optimisée pour la lisibilité sur petit écran

---

## 🔧 Configuration

### Google Mobile Ads

L'application est configurée pour intégrer des publicités :

```xml
<!-- AndroidManifest.xml -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-9096547068264021~8682164016"/>
```

⚠️ Remplacez cette valeur par votre propre ID AdMob en production.

### Orientation

L'application est verrouillée en **mode portrait** pour une meilleure expérience utilisateur.

---

## 📊 Fonctionnalités techniques

### Optimisations

- ✅ **Handler au lieu de Thread** pour le timer (meilleure gestion mémoire)
- ✅ **Configuration centralisée** (GameConfig) pour éviter les magic numbers
- ✅ **Helper pour animations** (ButtonAnimationHelper) pour réutilisabilité
- ✅ **Génération sans récursion** des calculs (évite StackOverflow)
- ✅ **Validation robuste** des divisions (résultats entiers uniquement)

### Edge-to-edge Support

```java
WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
    return insets;
});
```

---

## 🐛 Debugging

### Logs utiles

Ajoutez ces logs pour déboguer :

```java
// Dans Calculation.java
Log.d("Calculation", "Generated: " + val1 + " " + operations[operator] + " " + val2 + " = " + result);

// Dans Timer.java
Log.d("Timer", "Current time: " + currentSeconds + "s");

// Dans GameActivity.java
Log.d("Game", "Round " + currentRound + " - Score: " + rightAnswers + "/" + wrongAnswers);
```

---

## 🚦 Améliorations futures

### Fonctionnalités à venir

- [ ] Système de high scores avec SharedPreferences
- [ ] Mode multijoueur local
- [ ] Sons et effets sonores
- [ ] Vibrations haptiques
- [ ] Thème sombre
- [ ] Statistiques détaillées
- [ ] Système d'achievements
- [ ] Paramètres personnalisables (durée timer, difficulté)
- [ ] Support tablettes

### Optimisations techniques

- [ ] Migration vers Kotlin
- [ ] Architecture MVVM avec ViewModel
- [ ] Room Database pour persistance
- [ ] Coroutines pour opérations asynchrones
- [ ] Testing unitaire et UI
- [ ] CI/CD avec GitHub Actions

---

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 👨‍💻 Auteur

Développé avec ❤️ par **PixelMa**

---

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :

1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

---

## 📞 Support

- 📧 Email : support@digitclash.app
- 🐛 Issues : [GitHub Issues](https://github.com/votre-username/digitclash/issues)

---

<div align="center">

**Merci d'utiliser DigitClash ! Bon calcul ! 🚀**

Made with ☕ and 🎮

</div>