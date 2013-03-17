package org.alfresco.rule;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.UserTransaction;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;

import org.alfresco.repo.security.authentication.AuthenticationComponent;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.transaction.TransactionService;
import org.alfresco.util.ApplicationContextHelper;
import org.springframework.context.ApplicationContext;

import junit.framework.TestCase;

/**
 * 
 * Test class for update rule trigger review.
 * 
 * @author Rui Fernandes
 * 
 */
public class RuleReviewTest extends TestCase
{
	
	private static ApplicationContext applicationContext = ApplicationContextHelper
	        .getApplicationContext();
	protected NodeService nodeService;
	protected TransactionService transactionService;
	protected Repository repository;
	protected AuthenticationComponent authenticationComponent;
	
	/**
	 * 
	 * Sets services and current user as system.
	 * 
	 */
	@Override
	public void setUp()
	{
		nodeService = (NodeService) applicationContext.getBean("nodeService");
		authenticationComponent = (AuthenticationComponent) applicationContext
		        .getBean("authenticationComponent");
		transactionService = (TransactionService) applicationContext
		        .getBean("transactionComponent");
		repository = (Repository) applicationContext
		        .getBean("repositoryHelper");

		// Authenticate as the system user
		authenticationComponent.setSystemUserAsCurrentUser();
	}
	
	/**
	 * 
	 * Clears security context.
	 * 
	 */
	@Override
	public void tearDown()
	{
		authenticationComponent.clearCurrentSecurityContext();
	}
	
	/**
	 * 
	 * Tests that rule does not apply after unregistered aspect is added.
	 * 
	 * @throws Throwable
	 */
	public void testAddUnregisteredAspectNode() throws Throwable
	{
		UserTransaction trx = transactionService
		        .getUserTransaction();
		try
		{
			trx.begin();
			NodeRef node=createNode();
			trx.commit();
			trx = transactionService
			        .getUserTransaction();
			trx.begin();
			nodeService.addAspect(node, ContentModel.ASPECT_GEOGRAPHIC, null);
			trx.commit();
			trx = transactionService
			        .getUserTransaction();
			trx.begin();
			assertFalse(nodeService.hasAspect(node, ContentModel.ASPECT_DUBLINCORE));
			trx.commit();
		} catch (Throwable e)
		{
			try
			{
				trx.rollback();
			} catch (IllegalStateException ee)
			{
			}
			throw e;
		}
	}
	
	/**
	 * 
	 * Tests that rule applies after registered aspect is added.
	 * 
	 * @throws Throwable
	 */
	public void testAddAspectNode() throws Throwable
	{
		UserTransaction trx = transactionService
		        .getUserTransaction();
		try
		{
			trx.begin();
			NodeRef node=createNode();
			trx.commit();
			trx = transactionService
			        .getUserTransaction();
			trx.begin();
			nodeService.addAspect(node, ContentModel.ASPECT_VERSIONABLE, null);
			trx.commit();
			trx = transactionService
			        .getUserTransaction();
			trx.begin();
			assertTrue(nodeService.hasAspect(node, ContentModel.ASPECT_DUBLINCORE));
			trx.commit();
		} catch (Throwable e)
		{
			try
			{
				trx.rollback();
			} catch (IllegalStateException ee)
			{
			}
			throw e;
		}
	}
	
	
	
	/**
	 * 
	 * Tests that rule applies after property change.
	 * 
	 * @throws Throwable
	 */
	public void testUpdatePropertyNode() throws Throwable
	{
		UserTransaction trx = transactionService
		        .getUserTransaction();
		try
		{
			trx.begin();
			NodeRef node=createNode();
			trx.commit();
			trx = transactionService
			        .getUserTransaction();
			trx.begin();
			nodeService.setProperty(node, ContentModel.PROP_TITLE, "test-title-update");
			trx.commit();
			trx = transactionService
			        .getUserTransaction();
			trx.begin();
			assertTrue(nodeService.hasAspect(node, ContentModel.ASPECT_DUBLINCORE));
			trx.commit();
		} catch (Throwable e)
		{
			try
			{
				trx.rollback();
			} catch (IllegalStateException ee)
			{
			}
			throw e;
		}
	}
	
	
	/**
	 * Creates a sample node inside testrule space.
	 * 
	 * @throws Throwable
	 */
	private NodeRef createNode() throws Throwable
	{
		NodeRef companyHome = repository.getCompanyHome();
		NodeRef testruleSpace=nodeService.getChildByName(companyHome, ContentModel.ASSOC_CONTAINS, "testrule");
		String name = "Sample (" + System.currentTimeMillis() + ")";
		Map<QName, Serializable> contentProps = new HashMap<QName, Serializable>();
		contentProps.put(ContentModel.PROP_NAME, name);
		return nodeService.createNode(testruleSpace,
		        ContentModel.ASSOC_CONTAINS,
		        QName.createQName(NamespaceService.CONTENT_MODEL_PREFIX, name),
		        ContentModel.TYPE_CONTENT, contentProps).getChildRef();
	}

}
