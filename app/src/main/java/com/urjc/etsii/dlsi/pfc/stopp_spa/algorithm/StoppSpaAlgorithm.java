package com.urjc.etsii.dlsi.pfc.stopp_spa.algorithm;

import android.content.Context;

import com.urjc.etsii.dlsi.pfc.stopp_spa.database.MessageDAO;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Message;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.User;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.Word;
import com.urjc.etsii.dlsi.pfc.stopp_spa.model.WorkflowTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Created by vandia on 14/3/15.
 *
 * This class purpose is to manage the algorithm of interaction with user of STOPP_SPA aplication.
 */
public class StoppSpaAlgorithm {


    private User user;
    private MessageDAO messageDAO;
    private ApplyTemplates patterns;
    private Dictionaries keywords;
    private Inquiries inquiries;
    private Context context;

    //state algorithm variable
    private WorkflowTree actualPhase;
    private HashMap<String,String> substitutions=new HashMap<String,String>();
    private HashMap<String,Queue<Word>> phasesInquiries= new HashMap<String,Queue<Word>>();

    public StoppSpaAlgorithm(Context context, User user){
        this.user=user;
        this.context=context;
        messageDAO=MessageDAO.getInstance(context.getApplicationContext());
        patterns= ApplyTemplates.getInstance(context);
        keywords= Dictionaries.getInstance(context);
        inquiries=Inquiries.getInstance(context);
        substitutions.put(ApplyTemplates.USER_PATTERN, user.getName());
        actualPhase = new WorkflowTree(null, WorkflowTree.PhaseCategory.SALUTATION);
    }

    public Message nextMessage(Message m){
        Message result=null;
        boolean success=false;
        switch (actualPhase.getPhase()){
            case SALUTATION:
                result=getPhaseMessage();
                actualPhase =new WorkflowTree(actualPhase,WorkflowTree.PhaseCategory.PHASE_SENTIMIENTO);
                return result;

            case PHASE_SENTIMIENTO:
                if (actualPhase.getFather().getPhase()!= WorkflowTree.PhaseCategory.PHASE_SENTIMIENTO){
                    result=getPhaseMessage();
                    actualPhase=new WorkflowTree(actualPhase, WorkflowTree.PhaseCategory.PHASE_SENTIMIENTO);
                }else {
                    success=analizeAndFindInPHASE(m);
                    if (success){
                        result=getPhaseMessage();
                    }else {
                        //recursive call
                        result=nextMessage(m);
                    }
                }
                return result;

            case PHASE_SUB_SENTIMIENTO:
                success=interrogationPHASE(m);
                if (success){
                    result=getPhaseMessage();
                }else {
                    //recursive call
                    result=nextMessage(m);
                }
                return result;

            case PHASE_PROBLEMA:

                if (actualPhase.getFather().getPhase()!= WorkflowTree.PhaseCategory.PHASE_PROBLEMA){
                    result=getPhaseMessage();
                    actualPhase=new WorkflowTree(actualPhase,
                            WorkflowTree.PhaseCategory.PHASE_SENTIMIENTO);
                }else {
                    success = analizeAndFindInPHASE(m);
                    if (success) {
                        result = getPhaseMessage();
                    } else {
                        //recursive call
                        result = nextMessage(m);
                    }
                }
                return result;

            case PHASE_SUB_PROBLEMA:

                success=interrogationPHASE(m);
                if (success){
                    result=getPhaseMessage();
                }else {
                    //recursive call
                    result=nextMessage(m);
                }

            case PHASE_OBJETIVO:
                break;
            case POSITIVE_FINISHING:
                result=getPhaseMessage();
                actualPhase =new WorkflowTree(actualPhase,actualPhase.getPhase().getPositive());
                return result;
            case GOODBYES:
                break;
            case MISUNDERSTANDINGS:
                break;
            default:
        }
        return result;
    }

    private Message getPhaseMessage(){
        Message result=null;
        try {
            messageDAO.open();
            result = messageDAO.getMessage(actualPhase.getPhase(),user.getAgent());
            result.setMessage(patterns.Substitution(result.getMessage(),substitutions));
        }finally{
            messageDAO.close();
        }
        return result;
    }

    private Word dictionaryMatch(Message message, String dict){
        String messageContent=message.getMessage();
        String [] words=messageContent.split("\\P{Alpha}+");
        ArrayList<Word> dictionary=keywords.getDictionary(dict);

        Word wordMatched=null;
        for (int i = 0; i < words.length; i++) {
            String w= words[i];
            Word word=new Word(keywords.getCollator().getCollationKey(w),null);
            if (dictionary.contains(word)){
                //postive match of the words in the dictionary of feelings, problems etc
                wordMatched=dictionary.get(dictionary.indexOf(word));
            }
        }

        return wordMatched;

    }

    private boolean analizeAndFindInPHASE(Message m){
        Word searched= dictionaryMatch(m,actualPhase.getPhase().toString());
        if (searched !=null){
            switch (searched.getType()){
                case POSITIVE:
                    actualPhase=new WorkflowTree(actualPhase,
                            WorkflowTree.PhaseCategory.POSITIVE_FINISHING);
                    return true;
                case NEGATIVE:
                    actualPhase=new WorkflowTree(actualPhase,actualPhase.getPhase().getPositive());
                    substitutions.put(ApplyTemplates.FEELING_PATTERN, searched.getContent());
                    return true;
                default: return false;
            }
        }else {
            actualPhase=new WorkflowTree(actualPhase,actualPhase.getPhase().getNegative());
            return false;
        }
    }

    private boolean interrogationPHASE(Message message){
        Queue<Word> queries=phasesInquiries.get(actualPhase.getPhase().toString());
        if (queries==null){
            queries=inquiries.getInquiries(actualPhase.getPhase().toString());
            phasesInquiries.put(actualPhase.getPhase().toString(),queries);
        }
        if (actualPhase.getFather().getPhase()==actualPhase.getPhase()){
            Word searched=queries.poll();
            Word response= dictionaryMatch(message, Dictionaries.ANSWER);
            if ((response==null) || (response.getType()== Word.WordType.NEGATIVE)){
                actualPhase=new WorkflowTree(actualPhase,actualPhase.getPhase().getNegative());
                substitutions.put(ApplyTemplates.FEELING_PATTERN, queries.peek().getContent());
                return true;
            }else {
                if (searched!= null) {

                    switch (searched.getType()) {
                        case POSITIVE:
                            actualPhase = new WorkflowTree(actualPhase,
                                    WorkflowTree.PhaseCategory.POSITIVE_FINISHING);
                            return false;
                        case NEGATIVE:
                            actualPhase = new WorkflowTree(actualPhase, actualPhase.getPhase().getPositive());
                            substitutions.put(ApplyTemplates.FEELING_PATTERN, searched.getContent());
                            return false;

                        default: return false;
                    }
                }else{
                    actualPhase = new WorkflowTree(actualPhase,
                            WorkflowTree.PhaseCategory.POSITIVE_FINISHING);
                    return true;
                }
            }
        }else {
            actualPhase=new WorkflowTree(actualPhase,actualPhase.getPhase().getNegative());
            substitutions.put(ApplyTemplates.FEELING_PATTERN, queries.peek().getContent());
            return true;
        }
    }
}
