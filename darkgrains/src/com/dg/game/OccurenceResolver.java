/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game;

import com.dg.game.units.Battalion;
import com.dg.game.units.UnitPack;
import com.dg.game.units.Occurrence;
import com.dg.game.units.internal.Bandit;
import com.dg.game.units.internal.BanditStateEnum;
import com.dg.game.units.internal.Cop;
import com.dg.game.units.internal.Unit;
import com.dg.game.units.internal.UnitStateEnum;
import com.dg.util.FastRandom;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class OccurenceResolver {

    private Occurrence occ;
    private Battalion bat;
    private float acaoRapida;
    private int vantNum;
    private int bndDead, bndArrest;
    private int copDead, copInjury;

    public OccurenceResolver(Occurrence occ, Battalion bat) {
        this.occ = occ;
        this.bat = bat;
    }

    public boolean getResult() {
        if (bndDead + bndArrest <= copDead + copInjury) {
            return true;
        } else {
            return false;
        }
    }
    
    public void resolveOccurrence() {
        acaoRapida = occ.getRemaningTime();
        if (acaoRapida < 1f) {
            acaoRapida = 1f;
        }
        vantNum = bat.list.size() - occ.list.size();
        Logger.getGlobal().log(Level.INFO, "Resolcendo ocorrencia:\n\tOcc ==> {0}"
                + "\n\tBat ==> {1}\n\tAção Rápida: {2}\n\tVantagem Num: {3}",
                new Object[]{occ, bat, acaoRapida, vantNum});
        switch (testPersuasao()) {
            case 1:
                Logger.getGlobal().log(Level.INFO, "Vitória por persuasão....{0}", occ);
                //victory
                break;
            case 0:
                if (testFuga() > 0) {
                    Logger.getGlobal().log(Level.INFO, "Vitória por perseguição....{0}", occ);
                    //victory
                } else {
                    Logger.getGlobal().log(Level.INFO, "Derrota por perseguição....{0}", occ);
                    //defeat
                }
                break;
            case -1:
                if (testCombate() > 0) {
                    Logger.getGlobal().log(Level.INFO, "Vitória por combate....{0}", occ);
                    //victory
                } else {
                    Logger.getGlobal().log(Level.INFO, "Derrota por combate....{0}", occ);
                    //defeat
                }
                break;
        }
    }

    private int testCombate() {
        int j = 0;
        if (vantNum > 0) {
            for (int i = 0; i < bat.list.size(); i++, j++) {
                if (i >= occ.list.size()) {
                    j = 0;
                }
                resolvePair(bat.list.get(i), occ.list.get(j));
            }
        } else if (vantNum < 0) {
            for (int i = 0; i < occ.list.size(); i++, j++) {
                if (j >= bat.list.size()) {
                    j = 0;
                }
                resolvePair(bat.list.get(j), occ.list.get(i));
            }
        } else {
            for (int i = 0; i < occ.list.size(); i++, j++) {
                resolvePair(bat.list.get(i), occ.list.get(i));
            }
        }

        for (Bandit bnd : occ.list) {
            if (bnd.lifeState == UnitStateEnum.MORTO) {
                bndDead++;
            }
            if (bnd.getState() == BanditStateEnum.PRESO) {
                bndArrest++;
            }
        }
        for (Cop cop : bat.getList()) {
            if (cop.lifeState == UnitStateEnum.MORTO) {
                copDead++;
            }
            if (cop.lifeState == UnitStateEnum.FERIDO) {
                copInjury++;
            }
        }
        int result;

        if (bndDead + bndArrest <= copDead + copInjury) {
            result = 1;
        } else {
            result = -1;
        }
        Logger.getGlobal().log(Level.INFO, "Resultados do combate: {4}\n\t"
                + "Cops:\n\t\tMortos ==> {0}\n\t\tFeridos ==> {1}\n\t"
                + "Bandits:\n\t\tMortos ==> {2}\n\t\tPresos ==> {3}",
                new Object[]{copDead, copInjury, bndDead, bndArrest, result});
        return result;
    }

    private int testPersuasao() {
        int result;
        if (bat.potPersuasao(acaoRapida, vantNum) > occ.persuasaoMed()) {

            if (bat.potPersuasao(acaoRapida, vantNum) * 0.3f > occ.persuasaoMed()) {
                result = 1;
            } else {
                result = 0;
            }
        } else {
            result = -1;
        }
        Logger.getGlobal().log(Level.INFO, "Resultado persuasão: {0}\n\t"
                + "Battalion({1}) ==> {2}\n\t"
                + "Occurrence({3}) ==> {4}",
                new Object[]{result, bat, bat.potPersuasao(acaoRapida, vantNum), occ, occ.persuasaoMed()});
        return result;
    }

    private int testFuga() {
        if (bat.potPerseguisao() >= occ.potFuga()) {
            return 1;
        } else {
            return -1;
        }
    }

    private void resolveNonLetal(Unit unit) {
        if (unit instanceof Cop) {
            float rand = FastRandom.randValue(0, 100);
            rand += unit.getCondFisico() * 1.5f;
            if (rand < 20f) {
                ((Cop) unit).setAwayTime(60f);
                //set away true?
            } else if (rand < 53.3f) {
                ((Cop) unit).setAwayTime(60f * 2f);
            } else {
                ((Cop) unit).setAwayTime(60f * 3f);
            }
        } else if (unit instanceof Bandit) {
            ((Bandit) unit).setState(BanditStateEnum.PRESO);
        }
    }

    private void resolvePair(Cop cop, Bandit bnd) {
        if (cop.poderLuta(acaoRapida, vantNum) >= bnd.poderLuta()) {
            if (FastRandom.randValue(0, 100) < 50f) {
                resolveNonLetal(bnd);
            }
            if (occ.isLetal()) {
                if (FastRandom.randValue(0, 100) - bnd.getCondFisico() < 50f) {
                    bnd.lifeState = UnitStateEnum.MORTO;
                }
            }
        } else {
            if (FastRandom.randValue(0, 100) < 50f) {
                resolveNonLetal(cop);
            }
            if (occ.isLetal()) {
                if (FastRandom.randValue(0, 100) - bnd.getCondFisico() < 50f) {
                    cop.lifeState = UnitStateEnum.MORTO;
                }
            }
        }
    }
}
