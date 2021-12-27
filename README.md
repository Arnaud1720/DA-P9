# MyERP
### Lancement
    cd docker/dev
    docker-compose up
### Arrêt

    cd docker/dev
    docker-compose stop


### Remise à zero

    cd docker/dev
    docker-compose stop
    docker-compose rm -v
    docker-compose up

--------------------------------------------
### liste des erreurs
1) EcritureComptable.java
L-115
On compare des BigDecimal. On utilise compareTo et non equals


    public boolean isEquilibree() {
        return this.getTotalDebit().compareTo(getTotalCredit()) == 0;
    }
----------------------------------------------------------------------------
2) séparateur manquant ' , ' entre débit && crédit L-58 69 sqlContext.xml

            <property name="SQLinsertListLigneEcritureComptable">
            <value>
                INSERT INTO myerp.ligne_ecriture_comptable (
                ecriture_id, ligne_id, compte_comptable_numero, libelle, debit,
                credit
                ) VALUES (
                :ecriture_id, :ligne_id, :compte_comptable_numero, :libelle, :debit,
                :credit
                )
            </value>
        </property>


