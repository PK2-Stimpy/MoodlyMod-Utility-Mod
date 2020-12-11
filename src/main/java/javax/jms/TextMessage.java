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

/** A {@code TextMessage} object is used to send a message containing a 
  * {@code java.lang.String}.
  * It inherits from the {@code Message} interface and adds a text message 
  * body.
  *
  * <P>This message type can be used to transport text-based messages, including
  *  those with XML content.
  *
  * <P>When a client receives a {@code TextMessage}, it is in read-only 
  * mode. If a client attempts to write to the message at this point, a 
  * {@code MessageNotWriteableException} is thrown. If 
  * {@code clearBody} is 
  * called, the message can now be both read from and written to.
  *
  * @see         javax.jms.Session#createTextMessage()
  * @see         javax.jms.Session#createTextMessage(String)
  * @see         javax.jms.BytesMessage
  * @see         javax.jms.MapMessage
  * @see         javax.jms.Message
  * @see         javax.jms.ObjectMessage
  * @see         javax.jms.StreamMessage
  * @see         java.lang.String
  * 
  * @version JMS 2.0
  * @since JMS 1.0
  * 
  */
 
public interface TextMessage extends Message { 

    /** Sets the string containing this message's data.
      *  
      * @param string the {@code String} containing the message's data
      *  
      * @exception JMSException if the JMS provider fails to set the text due to
      *                         some internal error.
      * @exception MessageNotWriteableException if the message is in read-only 
      *                                         mode.
      */ 

    void
    setText(String string) throws JMSException;


    /** Gets the string containing this message's data.  The default
      * value is null.
      *  
      * @return the {@code String} containing the message's data
      *  
      * @exception JMSException if the JMS provider fails to get the text due to
      *                         some internal error.
      */ 

    String
    getText() throws JMSException;
}
