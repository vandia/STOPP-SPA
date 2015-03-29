package com.urjc.etsii.dlsi.pfc.stopp_spa.model;

/**
 * Created by vandia on 17/3/15.
 */
public class WorkflowTree {
    public enum PhaseCategory {
        SALUTATION("SALUTATION"),
        GOODBYES("GOODBYES"),
        MISUNDERSTANDINGS("MISUNDERSTANDINGS"),
        POSITIVE_FINISHING("POSITIVE_FINISHING"),
        PHASE_SENTIMIENTO("PHASE_SENTIMIENTO"),
        PHASE_SUB_SENTIMIENTO("PHASE_SUB_SENTIMIENTO"),
        PHASE_PROBLEMA("PHASE_PROBLEMA"),
        PHASE_SUB_PROBLEMA("PHASE_SUB_PROBLEMA"),
        PHASE_OBJETIVO("PHASE_OBJETIVO");


        private String name;

        PhaseCategory(String name){
            this.name=name;
        }



        public PhaseCategory getPositive(){
            switch (this) {
                case SALUTATION:
                    return PHASE_SENTIMIENTO;
                case GOODBYES:
                    return GOODBYES;
                case MISUNDERSTANDINGS:
                    return MISUNDERSTANDINGS;
                case POSITIVE_FINISHING:
                    return POSITIVE_FINISHING;
                case PHASE_SENTIMIENTO:
                    return PHASE_PROBLEMA;
                case PHASE_SUB_SENTIMIENTO:
                    return PHASE_PROBLEMA;
                case PHASE_PROBLEMA:
                    return PHASE_OBJETIVO;
                case PHASE_SUB_PROBLEMA:
                    return PHASE_OBJETIVO;
                case PHASE_OBJETIVO:
                    return PHASE_OBJETIVO;
                /*case PHASE_PIENSO:
                    return "PHASE_PIENSO";
                case PHASE_PREVEO:
                    return "PHASE_PREVEO";
                case PHASE_SELECCIONO:
                    return "PHASE_SELECCIONO";
                case PHASE_PLANEO:
                    return "PHASE_PLANEO";
                case PHASE_ADVIERTO:
                    return "PHASE_ADVIERTO";*/
                default: return null;

            }

        }

        public PhaseCategory getNegative(){

            switch (this) {
                case SALUTATION:
                    return SALUTATION;
                case GOODBYES:
                    return GOODBYES;
                case MISUNDERSTANDINGS:
                    return MISUNDERSTANDINGS;
                case POSITIVE_FINISHING:
                    return POSITIVE_FINISHING;
                case PHASE_SENTIMIENTO:
                    return PHASE_SUB_SENTIMIENTO;
                case PHASE_SUB_SENTIMIENTO:
                    return PHASE_SUB_SENTIMIENTO;
                case PHASE_PROBLEMA:
                    return PHASE_SUB_PROBLEMA;
                case PHASE_SUB_PROBLEMA:
                    return PHASE_SUB_PROBLEMA;
                case PHASE_OBJETIVO:
                    return PHASE_OBJETIVO;
                /*case PHASE_PIENSO:
                    return "PHASE_PIENSO";
                case PHASE_PREVEO:
                    return "PHASE_PREVEO";
                case PHASE_SELECCIONO:
                    return "PHASE_SELECCIONO";
                case PHASE_PLANEO:
                    return "PHASE_PLANEO";
                case PHASE_ADVIERTO:
                    return "PHASE_ADVIERTO";*/
                default: return null;

            }

        }

        @Override
        public String toString() {
            return this.name;
        }
    };


    private WorkflowTree father;
    private PhaseCategory phase;

    public WorkflowTree(WorkflowTree father, PhaseCategory phase) {
        this.father = father;
        this.phase = phase;

    }

    public WorkflowTree getFather() {
        return father;
    }

    public PhaseCategory getPhase() {
        return phase;
    }

}
