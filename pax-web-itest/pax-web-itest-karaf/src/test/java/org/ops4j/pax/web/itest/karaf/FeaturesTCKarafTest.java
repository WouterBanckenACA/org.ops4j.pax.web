/**
 * 
 */
package org.ops4j.pax.web.itest.karaf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;

/**
 * @author achim
 * 
 */
@RunWith(PaxExam.class)
@Ignore("Disabled because, this test doesn't work due to new resolver in Karaf 4")
public class FeaturesTCKarafTest extends KarafBaseTest {

	@Configuration
	public Option[] config() {
		return tomcatConfig();
	}

	@Test
	public void test() throws Exception {
		
		//this is needed since the test is a bit to fast :)
		Thread.sleep(2000);
		
		assertTrue(featuresService.isInstalled(featuresService
				.getFeature("pax-war-tomcat")));

	}
	
	@Test
	@Ignore
	public void testJmx() throws Exception {
		Thread.sleep(2000);
		
		JMXConnector connector = null;
        try {
            connector = this.getJMXConnector();
            MBeanServerConnection connection = connector.getMBeanServerConnection();
            ObjectName name = new ObjectName("Catalina:type=Server");
            Object handlers = connection.getAttribute(name, "stateName");
            assertNotNull(handlers);
        } finally {
            if (connector != null)
                connector.close();
        }
	}

	   public JMXConnector getJMXConnector() throws Exception {
		   	JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + RMI_REG_PORT+ "/karaf-root");
	        Hashtable<String, Object> env = new Hashtable<String, Object>();
	        String[] credentials = new String[]{ "karaf", "karaf" };
	        env.put("jmx.remote.credentials", credentials);
	        JMXConnector connector = JMXConnectorFactory.connect(url, env);
	        return connector;
	    }
	
}
