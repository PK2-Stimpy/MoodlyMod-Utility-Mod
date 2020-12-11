/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.jms;

/** A {@code TopicConnection} object is an active connection to a 
  * publish/subscribe JMS provider. A client uses a {@code TopicConnection} 
  * object to create one or more {@code TopicSession} objects
  * for producing and consuming messages.
  *
  *<P>A {@code TopicConnection} can be used to create a 
  *{@code TopicSession}, from which
  * specialized topic-related objects can be created. 
  * A more general, and recommended approach is to use the 
  * {@code Connection} object. 
  *
  *
  * <P>The {@code TopicConnection} object
  * should be used to support existing code.
  *
  * @see         javax.jms.Connection
  * @see         javax.jms.ConnectionFactory
  * @see	 javax.jms.TopicConnectionFactory
  * 
  * @version JMS 2.0
  * @since JMS 1.0
  * 
  */

public interface TopicConnection extends Connection {

    /** 
     * Creates a {@code TopicSession} object, 
     * specifying {@code transacted} and {@code acknowledgeMode}.
     * <p> 
     * The effect of setting the {@code transacted} and {@code acknowledgeMode} 
     * arguments depends on whether this method is called in a Java SE environment, 
     * in the Java EE application client container, or in the Java EE web or EJB container.
     * If this method is called in the Java EE web or EJB container then the 
     * effect of setting the transacted} and {@code acknowledgeMode} 
     * arguments also depends on whether or not there is an active JTA transaction 
     * in progress.  
     * <p>
     * In a <b>Java SE environment</b> or in <b>the Java EE application client container</b>:
     * <ul>
     * <li>If {@code transacted} is set to {@code true} then the session 
     * will use a local transaction which may subsequently be committed or rolled back 
     * by calling the session's {@code commit} or {@code rollback} methods. 
     * The argument {@code acknowledgeMode} is ignored.
     * <li>If {@code transacted} is set to {@code false} then the session 
     * will be non-transacted. In this case the argument {@code acknowledgeMode}
     * is used to specify how messages received by this session will be acknowledged.
     * The permitted values are 
     * {@code Session.CLIENT_ACKNOWLEDGE}, 
     * {@code Session.AUTO_ACKNOWLEDGE} and
     * {@code Session.DUPS_OK_ACKNOWLEDGE}.
     * For a definition of the meaning of these acknowledgement modes see the links below.
     * </ul>
     * <p>
     * In a <b>Java EE web or EJB container, when there is an active JTA transaction in progress</b>:
     * <ul>
     * <li>Both arguments {@code transacted} and {@code acknowledgeMode} are ignored.
     * The session will participate in the JTA transaction and will be committed or rolled back
     * when that transaction is committed or rolled back, 
     * not by calling the session's {@code commit} or {@code rollback} methods.
     * Since both arguments are ignored, developers are recommended to use 
     * {@code createSession()}, which has no arguments, instead of this method.
     * </ul>
     * <p>
     * In the <b>Java EE web or EJB container, when there is no active JTA transaction in progress</b>:
     * <ul>
     * <li>
     * If {@code transacted} is set to false and {@code acknowledgeMode} is set to
     * {@code JMSContext.AUTO_ACKNOWLEDGE} or {@code Session.DUPS_OK_ACKNOWLEDGE} then the
     * session will be non-transacted and messages will be acknowledged according
     * to the value of {@code acknowledgeMode}.
     * <li>
     * If {@code transacted} is set to false and {@code acknowledgeMode} is set to
     * {@code JMSContext.CLIENT_ACKNOWLEDGE} then the JMS provider is recommended to
     * ignore the specified parameters and instead provide a non-transacted,
     * auto-acknowledged session. However the JMS provider may alternatively
     * provide a non-transacted session with client acknowledgement.
     * <li>
     * If {@code transacted} is set to true, then the JMS provider is recommended to
     * ignore the specified parameters and instead provide a non-transacted,
     * auto-acknowledged session. However the JMS provider may alternatively
     * provide a local transacted session.
     * <li>
     * Applications are recommended to set {@code transacted} to false and
     * {@code acknowledgeMode} to {@code JMSContext.AUTO_ACKNOWLEDGE} or
     * {@code Session.DUPS_OK_ACKNOWLEDGE} since since applications which set
     * {@code transacted} to false and set {@code acknowledgeMode} to
     * {@code JMSContext.CLIENT_ACKNOWLEDGE}, or which set {@code transacted} 
     * to true, may not be portable. 
     * </ul> 
     * <p>
     * Applications running in the Java EE web and EJB containers must not attempt 
     * to create more than one active (not closed) {@code Session} object per connection. 
     * If this method is called in a Java EE web or EJB container when an active
     * {@code Session} object already exists for this connection then a {@code JMSException} may be thrown.
     * 
     * @param transacted indicates whether the session will use a local transaction,
     * except in the cases described above when this value is ignored.
     * 
     * @param acknowledgeMode when transacted is false, indicates how messages received
     * by the session will be acknowledged, except in the cases described above
     * when this value is ignored.
     * 
     * @return a newly created {@code TopicSession}
     *  
     * @exception JMSException if the {@code TopicConnection} object fails
     *                         to create a {@code TopicSession} due to 
     *                         <ul>
     *                         <li>some internal error, 
     *                         <li>lack of support for the specific transaction and acknowledgement mode, or
     *                         <li>because this method is being called in a Java EE web or EJB application 
     *                         and an active session already exists for this connection.
     *                         </ul>
     * @since JMS 1.1
     *
     * @see Session#AUTO_ACKNOWLEDGE 
     * @see Session#CLIENT_ACKNOWLEDGE 
     * @see Session#DUPS_OK_ACKNOWLEDGE 
     * 
     */

    TopicSession
    createTopicSession(boolean transacted,
                       int acknowledgeMode) throws JMSException;


    /** Creates a connection consumer for this connection (optional operation).
      * This is an expert facility not used by regular JMS clients.
      *  
      * @param topic the topic to access
      * @param messageSelector only messages with properties matching the
      * message selector expression are delivered.  A value of null or
      * an empty string indicates that there is no message selector  
      * for the message consumer.
      * @param sessionPool the server session pool to associate with this 
      * connection consumer
      * @param maxMessages the maximum number of messages that can be
      * assigned to a server session at one time
      *
      * @return the connection consumer
      *
      * @exception JMSException if the {@code TopicConnection} object fails
      *                         to create a connection consumer due to some
      *                         internal error or invalid arguments for 
      *                         {@code sessionPool} and 
      *                         {@code messageSelector}.
      * @exception InvalidDestinationException if an invalid topic is specified.
      * @exception InvalidSelectorException if the message selector is invalid.
      * @see javax.jms.ConnectionConsumer
      */ 

    ConnectionConsumer
    createConnectionConsumer(Topic topic,
                             String messageSelector,
                             ServerSessionPool sessionPool,
			     int maxMessages)
			     throws JMSException;


    /** Create a durable connection consumer for this connection (optional operation). 
      * This is an expert facility not used by regular JMS clients.
      *                
      * @param topic the topic to access
      * @param subscriptionName durable subscription name
      * @param messageSelector only messages with properties matching the
      * message selector expression are delivered.  A value of null or
      * an empty string indicates that there is no message selector 
      * for the message consumer.
      * @param sessionPool the server session pool to associate with this 
      * durable connection consumer
      * @param maxMessages the maximum number of messages that can be
      * assigned to a server session at one time
      *
      * @return the durable connection consumer
      *  
      * @exception JMSException if the {@code TopicConnection} object fails
      *                         to create a connection consumer due to some
      *                         internal error or invalid arguments for 
      *                         {@code sessionPool} and 
      *                         {@code messageSelector}.
      * @exception InvalidDestinationException if an invalid topic is specified.
      * @exception InvalidSelectorException if the message selector is invalid.
      * @see javax.jms.ConnectionConsumer
      */

    ConnectionConsumer
    createDurableConnectionConsumer(Topic topic,
				    String subscriptionName,
                                    String messageSelector,
                                    ServerSessionPool sessionPool,
				    int maxMessages)
                             throws JMSException;
}
