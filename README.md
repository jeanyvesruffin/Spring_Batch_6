# Spring_Batch_6

## Architecture de Spring Batch 6

```mermaid
graph TD
    %% Définition des blocs avec largeur identique
    A["<div style='width:220px; text-align:center;'>Application</div>"]
    B["<div style='width:220px; text-align:center;'>Batch Core</div>"]
    C["<div style='width:220px; text-align:center;'>Batch Infrastructure</div>"]

    %% 1. On force la colonne centrale (l'épine dorsale) de haut en bas
    A --> B
    B --> C

    %% 2. On ajoute les flèches de retour pointillées
    B -.-> A
    C -.-> B

    %% 3. On ajoute les flèches latérales de contournement (sans les rallonger)
    A --> C
    C -.-> A

    %% Styles pour les couleurs exactes
    style A fill:#54a935,color:black,stroke:none,font-weight:bold
    style B fill:#4781a2,color:black,stroke:none,font-weight:bold
    style C fill:#fcc004,color:black,stroke:none,font-weight:bold
```
