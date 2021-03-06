package com.dummy.myerp.testconsumer.consumer;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


public class ComptabiliteDaoImplIntegrationTest extends ConsumerTestCase {

    @Test
    public void getListCompteComptableShouldReturnList() {

        List<CompteComptable> compteComptableList = getDaoProxy().getComptabiliteDao().getListCompteComptable();
        assertThat(compteComptableList).isNotEmpty();

    }

    @Test
    public void getListJournalComptableShouldReturnList() {
        List<JournalComptable> journalComptableList = getDaoProxy().getComptabiliteDao().getListJournalComptable();
        assertThat(journalComptableList).isNotEmpty();

    }

    @Test
    public void getListEcritureComptableShouldReturnList() {
        List<EcritureComptable> ecritureComptableList = getDaoProxy().getComptabiliteDao().getListEcritureComptable();
        assertThat(ecritureComptableList).isNotEmpty();

    }
    @Test
    public void getEcritureComptableShouldReturnEcritureComptable() throws NotFoundException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStringTest = "2022-01-30";

        EcritureComptable ecritureComptable;
        try {
            ecritureComptable = getDaoProxy().getComptabiliteDao().getEcritureComptable(-1);
            assertThat(ecritureComptable).isNotNull();
            assertThat(ecritureComptable.getReference()).isEqualTo("TE-2022/00009");
            assertThat(ecritureComptable.getJournal().getCode()).isEqualTo("TE");
            Date dateTest = formatter.parse(dateStringTest);
            assertThat(ecritureComptable.getDate()).isEqualTo(dateTest);
            assertThat(ecritureComptable.getLibelle()).isEqualTo("Cartouches d???imprimante TE");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NotFoundException.class)
    public void getEcritureComptableByIdShouldReturnException() throws NotFoundException {
        getDaoProxy().getComptabiliteDao().getEcritureComptable(-10);
    }


    @Test
    public void getEcritureComptableByRefShouldReturnEcritureComptable() throws NotFoundException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStringTest = "2016-12-29";

        EcritureComptable ecritureComptable;
        try {
            ecritureComptable = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef("BQ-2016/00003");
            assertThat(ecritureComptable).isNotNull();
            assertThat(ecritureComptable.getId()).isEqualTo(47);
            assertThat(ecritureComptable.getJournal().getCode()).isEqualTo("BQ");
            Date dateTest = formatter.parse(dateStringTest);
            assertThat(ecritureComptable.getDate()).isEqualTo(dateTest);
            assertThat(ecritureComptable.getLibelle()).isEqualTo("Paiement Facture F110001");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NotFoundException.class)
    public void getEcritureComptableByReferenceShouldReturnException() throws NotFoundException {
        getDaoProxy().getComptabiliteDao().getEcritureComptableByRef("VE-2016/00010");
    }

    @Test
    public void loadListLigneEcritureByAccountNumbers() throws NotFoundException {
        EcritureComptable ecritureComptable = getDaoProxy().getComptabiliteDao().getEcritureComptable(50);
        getDaoProxy().getComptabiliteDao().loadListLigneEcriture(ecritureComptable);
        assertThat(ecritureComptable.getListLigneEcriture().get(0).getCompteComptable().getNumero()).isEqualTo(411);
        assertThat(ecritureComptable.getListLigneEcriture().get(1).getCompteComptable().getNumero()).isEqualTo(706);
    }

    @Test
    public void insertEcritureComptable() {

        List<EcritureComptable> listEcritureComptable = getDaoProxy().getComptabiliteDao().getListEcritureComptable();
        EcritureComptable ecritureComptable = listEcritureComptable.get(listEcritureComptable.size() - 1);
        int sequence = Integer.parseInt(ecritureComptable.getReference().substring(8)) + 1;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy");

        ecritureComptable.setJournal(new JournalComptable("TE", "Tests"));
        ecritureComptable.setLibelle("Integration Test INSERT");
        ecritureComptable.setDate(new Date());
        int annee = Integer.parseInt(formater.format(ecritureComptable.getDate()));
        ecritureComptable.setReference(ecritureComptable.getJournal().getCode() + "-" + annee + "/" + String.format("%05d", sequence));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(200),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(200)));

        getDaoProxy().getComptabiliteDao().insertEcritureComptable(ecritureComptable);
        assertThat(ecritureComptable.getId()).isNotNull();
    }

    @Test
    public void updateEcritureComptable() {

        List<EcritureComptable> vEcritureComptableList = getDaoProxy().getComptabiliteDao().getListEcritureComptable();
        for (EcritureComptable vEcritureComptable : vEcritureComptableList) {
            if (vEcritureComptable.getId() == -5) {
                vEcritureComptable.setLibelle("EXP");
                getDaoProxy().getComptabiliteDao().updateEcritureComptable(vEcritureComptable);
                assertEquals("Mise a jour reussie", "EXP", vEcritureComptable.getLibelle());
            }
        }
    }

    @Test
    public void deleteEcritureComptableById() throws NotFoundException {

        EcritureComptable ecritureComptable = getDaoProxy().getComptabiliteDao().getEcritureComptable(10);
        int initialSizeList = getDaoProxy().getComptabiliteDao().getListEcritureComptable().size();
        getDaoProxy().getComptabiliteDao().deleteEcritureComptable(ecritureComptable.getId());
        int sizeList = getDaoProxy().getComptabiliteDao().getListEcritureComptable().size();
        assertThat(sizeList).isLessThan(initialSizeList);
    }

    @Test
    public void getListSequenceEcritureComptableShouldReturnList(){
        List<SequenceEcritureComptable> sequenceEcritureComptableList = getDaoProxy().getComptabiliteDao().getListSequenceEcritureComptable();
        assertThat(sequenceEcritureComptableList).isNotEmpty();
    }

    @Test
    public void getLastSequenceEcritureComptableShouldReturnLastSequence() throws NotFoundException {
        SequenceEcritureComptable lastSequence = getDaoProxy().getComptabiliteDao().getSequenceEcritureComptable("TE", 2022);
        assertThat(lastSequence).isNotNull();
        assertThat(lastSequence.getDerniereValeur()).isEqualTo(98);
    }

    @Test
    public void insertSequenceEcritureComptableShouldReturnSequence(){
        int sizeInit = getDaoProxy().getComptabiliteDao().getListSequenceEcritureComptable().size();
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setJournalCode("TE");
        sequenceEcritureComptable.setAnnee(2021);
        sequenceEcritureComptable.setDerniereValeur(195);
        getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(sequenceEcritureComptable);
        int sizeFinal = getDaoProxy().getComptabiliteDao().getListSequenceEcritureComptable().size();
        assertThat(sizeInit).isLessThan(sizeFinal);
    }


}
