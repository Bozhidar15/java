package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;

import java.util.*;

public class Rule {
    private Queue<String> subjectIncludes = null;
    private Queue<String> subjectAndBodyIncludes = null;
    private String from = null;
    private Queue<String> recipientsIncludes = null;
    private List<String> folderPath;
    private String ruleDefinition;
    private int priority;
    private boolean isLegitRule = true;
    private void editListOfCriteria(String criteria, Queue<String> specified) {
        String withoutSpace = criteria.strip();
        String[] words = withoutSpace.split(", ");
        specified.addAll(Arrays.asList(words));
    }

    private void splitRuleDefinition(String ruleDef) {
        String[] words = ruleDef.split(System.lineSeparator());
        int subjInc = 0;
        int subjBodyInc = 0;
        int recipInc = 0;
        int fromInc = 0;
        for (var current : words ) {
            if (subjInc > 1 || subjBodyInc > 1 || recipInc > 1 || fromInc > 1) {
                isLegitRule = false;
                throw new RuleAlreadyDefinedException("One of the conditions is already defined");
            } else if (current.startsWith("subject-includes:")) {
                subjInc++;
                subjectIncludes = new LinkedList<>();
                editListOfCriteria(current.substring(current.indexOf(":") + 1), subjectIncludes);
            } else if (current.startsWith("subject-or-body-includes:")) {
                subjBodyInc++;
                subjectAndBodyIncludes = new LinkedList<>();
                editListOfCriteria(current.substring(current.indexOf(":") + 1), subjectAndBodyIncludes);
            } else if (current.startsWith("recipients-includes:")) {
                recipInc++;
                recipientsIncludes = new LinkedList<>();
                editListOfCriteria(current.substring(current.indexOf(":") + 1), recipientsIncludes);
            } else if (current.startsWith("from:")) {
                fromInc++;
                from = current.substring(current.indexOf(":") + 1).strip();
            }
        }
    }

    Rule(List<String> enteredFolderPath, String ruleDef, int enteredPriority) {
        folderPath = enteredFolderPath;
        ruleDefinition = ruleDef;
        priority = enteredPriority;
        splitRuleDefinition(ruleDef);
    }

    public Queue<String> getSubjectIncludes() {
        return subjectIncludes;
    }
    public Queue<String> getSubjectAndBodyIncludes() {
        return subjectAndBodyIncludes;
    }
    public String getFrom() {
        return from;
    }
    public Queue<String> getRecipientsIncludes() {
        return recipientsIncludes;
    }
    public boolean isLegitRule() {
        return isLegitRule;
    }
    public List<String> getFolderPath() {
        return folderPath;
    }
    public int getPriority() {
        return priority;
    }
}
