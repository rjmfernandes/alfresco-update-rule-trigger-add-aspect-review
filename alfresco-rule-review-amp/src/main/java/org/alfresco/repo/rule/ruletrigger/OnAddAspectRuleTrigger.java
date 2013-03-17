package org.alfresco.repo.rule.ruletrigger;

import java.util.List;

import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * On Add Aspect rule trigger.
 * 
 * @author Rui Fernandes
 * 
 */
public class OnAddAspectRuleTrigger extends RuleTriggerAbstractBase implements
        NodeServicePolicies.OnAddAspectPolicy
{

	private static final String ON_ADD_ASPECT = "onAddAspect";
	/**
	 * The logger
	 */
	private static Log logger = LogFactory.getLog(OnAddAspectRuleTrigger.class);

	/** True trigger parent rules, false otherwise */
	private boolean triggerParentRules = true;

	private List<String> aspects = null;

	/**
	 * @param aspects
	 */
	public void setAspects(List<String> aspects)
	{
		this.aspects = aspects;
	}

	/**
	 * Indicates whether the parent rules should be triggered or the rules on
	 * the node itself
	 * 
	 * @param triggerParentRules
	 *            true trigger parent rules, false otherwise
	 */
	public void setTriggerParentRules(boolean triggerParentRules)
	{
		this.triggerParentRules = triggerParentRules;
	}

	@Override
	public void onAddAspect(NodeRef nodeRef, QName aspectName)
	{
		if (!areRulesEnabled())
		{
			return;
		}

		if (aspects != null && !aspects.contains(aspectName.getPrefixString()))
		{
			return;
		}

		triggerRules(nodeRef);

	}

	/**
	 * @param nodeRef
	 */
	private void triggerRules(NodeRef nodeRef)
	{
		if (triggerParentRules == true)
		{
			List<ChildAssociationRef> parentsAssocRefs = this.nodeService
			        .getParentAssocs(nodeRef);
			for (ChildAssociationRef parentAssocRef : parentsAssocRefs)
			{
				triggerRules(parentAssocRef.getParentRef(), nodeRef);
				if (logger.isDebugEnabled() == true)
				{
					logger.debug("OnAddAspect rule triggered (parent); "
					        + "nodeRef=" + parentAssocRef.getParentRef());
				}
			}
		} else
		{
			triggerRules(nodeRef, nodeRef);
			if (logger.isDebugEnabled() == true)
			{
				logger.debug("OnAddAspect rule triggered; nodeRef=" + nodeRef);
			}
		}
	}

	@Override
	public void registerRuleTrigger()
	{
		// Bind behaviour
		this.policyComponent
		        .bindClassBehaviour(QName.createQName(
		                NamespaceService.ALFRESCO_URI, ON_ADD_ASPECT), this,
		                new JavaBehaviour(this, ON_ADD_ASPECT));

	}
}
