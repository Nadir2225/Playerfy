# Playerfy

**Author:** EL OUADGHIRI Nadir  
**Year:** 2024/2025  

---

## Introduction
Playerfy est une application Android de lecteur de musique, conçue en utilisant **Jetpack Compose** pour la création d'interfaces utilisateur modernes et dynamiques, et l'API **MediaStore** d'Android pour accéder aux fichiers audio stockés localement. Cette application permet de parcourir les fichiers audio, de gérer la lecture et de contrôler la musique en arrière-plan. Elle utilise un composant `MusicService` pour la gestion de la lecture audio, permettant aux utilisateurs d'interagir via des commandes de diffusion, organisées par un utilitaire `BroadcastHelper` pour assurer une coordination fluide des commandes de contrôle entre les composants. 

La gestion des diffusions est centralisée via un `ViewModel`, permettant une mise à jour efficace de l'état de la lecture audio et une organisation des métadonnées des fichiers, telles que le titre, l'artiste, la pochette d'album, la durée et le chemin du fichier. L'application extrait également les noms de dossiers pour offrir une navigation structurée et simplifiée des fichiers audio. La structure de Playerfy est modulaire, séparant les couches d’interface utilisateur, de modèle de données et de services pour assurer une évolutivité et une maintenance aisées, tout en offrant une expérience utilisateur optimale.

## Interfaces

### Interface principale
L'interface principale affiche tous les fichiers audio disponibles dans le stockage de l'appareil. En haut de l'écran, un bouton de lecture permet de démarrer la lecture du premier fichier audio de la liste.

<img src="https://github.com/user-attachments/assets/598278d0-cce2-4883-bd27-841d9aaf67a9" style="width: 200px"/>

### Interface de contrôle
L'interface de contrôle offre des fonctionnalités telles que la lecture, la pause, la reprise et le changement de piste audio. L'arrière-plan de cette interface change en fonction des couleurs dominantes de la pochette de l'album en cours de lecture, offrant une expérience visuelle immersive.<br>
<img src="https://github.com/user-attachments/assets/0323b3a0-b411-43ba-b777-32bc08920e5b" style="width: 200px"/>
<img src="https://github.com/user-attachments/assets/4f796d88-8227-4162-ac64-98d480b7822e" style="width: 200px"/>
