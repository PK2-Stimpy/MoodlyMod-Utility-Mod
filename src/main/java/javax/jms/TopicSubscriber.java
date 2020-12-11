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

/** A client uses a {@code TopicSubscriber} object to receive messages that
  * have been published to a topic. A {@code TopicSubscriber} object is the
  * publish/subscribe form of a message consumer. A {@code MessageConsumer}
  * can be created by using {@code Session.createConsumer}. 
  *
  * <P>A {@code TopicSession} allows the creation of multiple 
  * {@code TopicSubscriber} objects per topic.  It will deliver each 
  * message for a topic to each
  * subscriber eligible to receive it. Each copy of the message
  * is treated as a completely separate message. Work done on one copy has
  * no effect on the others; acknowledging one does not acknowledge the
  * others; one message may be delivered immediately, while another waits
  * for its subscriber to process messages ahead of it.
  *
  * <P>Regular {@code TopicSubscriber} objects are not durable. They 
  * receive only messages that are published while they are active.
  *
  * <P>Messages filtered out by a subscriber's message selector will never 
  * be delivered to the subscriber. From the subscriber's perspective, they 
  * do not exist.
  *
  * <P>In some cases, a connection may both publish and subscribe to a topic.
  * The subscriber {@code NoLocal} attribute allows a subscriber to inhibit
  * the 
  * delivery of messages published by its own connection.
  *
  * <P>If a client needs to receive all the messages published on a topic, 
  * including the ones published while the subscriber is inactive, it uses 
  * a durable {@code TopicSubscriber}. The JMS provider retains a record of
  * this durable 
  * subscription and insures that all messages from the topic's publishers 
  * are retained until they are acknowledged by this durable 
  * subscriber or they have expired.
  *
  * <P>Sessions with durable subscribers must always provide the same client 
  * identifier. In addition, each client must specify a name that uniquely 
  * identifies (within client identifier) each durable subscription it creates.
  * Only one session at a time can have a {@code TopicSubscriber} for a 
  * particular durable subscription. 
  *
  * <P>A client can change an existing durable subscription by creating a 
  * durable {@code TopicSubscriber} with the same name and a new topic 
  * and/or message 
  * selector. Changing a durable subscription is equivalent to unsubscribing 
  * (deleting) the old one and creating a new one.
  *
  * <P>The {@code unsubscribe} method is used to delete a durable 
  * subscription. The {@code unsubscribe} method can be used at the 
  * {@code Session} or {@code TopicSession} level.
  * This method deletes the state being 
  * maintained on behalf of the subscriber by its provider.
  *
  * <P>Creating a {@code MessageConsumer} provides the same features as
  * creating a {@code TopicSubscriber}. To create a durable subscriber, 
  * use of {@code Session.CreateDurableSubscriber} is recommended. The 
  * {@code TopicSubscriber} is provided to support existing code.
  * 
  * @see         javax.jms.Session#createConsumer
  * @see         javax.jms.Session#createDurableSubscriber
  * @see         javax.jms.TopicSession
  * @see         javax.jms.TopicSession#createSubscriber
  * @see         javax.jms.MessageConsumer
  * 
  * @version JMS 2.0
  * @since JMS 1.0
  * 
  */

public interface TopicSubscriber extends MessageConsumer {

    /** Gets the {@code Topic} associated with this subscriber.
      *  
      * @return this subscriber's {@code Topic}
      *  
      * @exception JMSException if the JMS provider fails to get the topic for
      *                         this topic subscriber
      *                         due to some internal error.
      */ 

    Topic
    getTopic() throws JMSException;


    /** Gets the {@code NoLocal} attribute for this subscriber. 
      * The default value for this attribute is false.
      *  
      * @return true if locally published messages are being inhibited
      *  
      * @exception JMSException if the JMS provider fails to get the
      *                         {@code NoLocal} attribute for
      *                         this topic subscriber
      *                         due to some internal error.
      */ 

    boolean
    getNoLocal() throws JMSException;
}
