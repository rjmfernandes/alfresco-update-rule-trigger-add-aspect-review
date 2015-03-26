Example of review of the update rule trigger (add aspect included as trigger).
This is a proof of concept: do not use the produced amp directly.
The interesting files should be:

https://code.google.com/p/alfresco-update-rule-trigger-add-aspect-review/source/browse/alfresco-rule-review-amp/src/main/amp/config/alfresco/module/alfresco-rule-review-amp/context/service-context.xml

https://code.google.com/p/alfresco-update-rule-trigger-add-aspect-review/source/browse/alfresco-rule-review-amp/src/main/java/org/alfresco/repo/rule/ruletrigger/OnAddAspectRuleTrigger.java

The example makes the update rule sensitive to the add aspect event (by default its not). In particular the presented implementation allows for configuring also which aspects should trigger the update rule when added so that not necessary will trigger the rule for any aspect added.

To setup the update rule to be also sensitive to the removal of aspect should be just analogous.

The implementation of the [OnAddAspectRuleTrigger](https://code.google.com/p/alfresco-update-rule-trigger-add-aspect-review/source/browse/alfresco-rule-review-amp/src/main/java/org/alfresco/repo/rule/ruletrigger/OnAddAspectRuleTrigger.java) is based  on the original implementation by Roy Wetherall of the [OnPropertyUpdateRuleTrigger](http://svn.alfresco.com/repos/alfresco-open-mirror/alfresco/HEAD/root/projects/repository/source/java/org/alfresco/repo/rule/ruletrigger/OnPropertyUpdateRuleTrigger.java)