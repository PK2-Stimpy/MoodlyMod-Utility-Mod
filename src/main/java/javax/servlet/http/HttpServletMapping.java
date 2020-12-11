/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

package javax.servlet.http;

/**
 * <p>Allows runtime discovery of the manner in which the {@link
 * HttpServlet} for the current {@link HttpServletRequest} was invoked.
 * Invoking any of the methods must not block the caller.  The
 * implementation must be thread safe.  Instances are immutable and are
 * returned from {@link HttpServletRequest#getHttpServletMapping}.</p>
 *
 * <p>Following are some illustrative examples for various combinations
 * of mappings.  Consider the following Servlet declaration:</p>
 *
 * <pre><code>
 * &lt;servlet&gt;
 *     &lt;servlet-name&gt;MyServlet&lt;/servlet-name&gt;
 *     &lt;servlet-class&gt;MyServlet&lt;/servlet-class&gt;
 * &lt;/servlet&gt;
 * &lt;servlet-mapping&gt;
 *     &lt;servlet-name&gt;MyServlet&lt;/servlet-name&gt;
 *     &lt;url-pattern&gt;/MyServlet&lt;/url-pattern&gt;
 *     &lt;url-pattern&gt;""&lt;/url-pattern&gt;
 *     &lt;url-pattern&gt;*.extension&lt;/url-pattern&gt;
 *     &lt;url-pattern&gt;/path/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </code></pre>
 *
 * <p>The expected values of the properties for various incoming URI
 * path values are as shown in this table.  The {@code servletName}
 * column is omitted as its value is always {@code MyServlet}.</p>
 * 
 * <table border="1">
 *   <caption>Expected values of properties for various URI paths</caption>
 *   <tr>
 *     <th>URI Path (in quotes)</th>
 *     <th>matchValue</th>
 *     <th>pattern</th>
 *     <th>mappingMatch</th>
 *   </tr>
 *   <tr>
 *     <td>""</td>
 *     <td>""</td>
 *     <td>""</td>
 *     <td>CONTEXT_ROOT</td>
 *   </tr>
 *   <tr>
 *     <td>"/index.html"</td>
 *     <td>""</td>
 *     <td>/</td>
 *     <td>DEFAULT</td>
 *   </tr>
 *   <tr>
 *     <td>"/MyServlet"</td>
 *     <td>MyServlet</td>
 *     <td>/MyServlet</td>
 *     <td>EXACT</td>
 *   </tr>
 *   <tr>
 *     <td>"/foo.extension"</td>
 *     <td>foo</td>
 *     <td>*.extension</td>
 *     <td>EXTENSION</td>
 *   </tr>
 *   <tr>
 *     <td>"/path/foo"</td>
 *     <td>foo</td>
 *     <td>/path/*</td>
 *     <td>PATH</td>
 *   </tr>  
 *   
 * </table>
 * 
 * @since 4.0
 */
public interface HttpServletMapping {


    
    /**
     * <p>Return the portion of the URI path that caused this request to
     * be matched.  If the {@link getMappingMatch} value is {@code
     * CONTEXT_ROOT} or {@code DEFAULT}, this method must return the
     * empty string.  If the {@link getMappingMatch} value is {@code
     * EXACT}, this method must return the portion of the path that
     * matched the servlet, omitting any leading slash.  If the {@link
     * getMappingMatch} value is {@code EXTENSION} or {@code PATH}, this
     * method must return the value that matched the '*'.  See the class
     * javadoc for examples. </p>
     * 
     * @return the match.
     * 
     * @since 4.0
     */
    public String getMatchValue();

    /**
     * <p>Return the String representation for the {@code url-pattern}
     * for this mapping.  If the {@link getMappingMatch} value is {@code
     * CONTEXT_ROOT} or {@code DEFAULT}, this method must return the
     * empty string. If the {@link getMappingMatch} value is {@code
     * EXTENSION}, this method must return the pattern, without any
     * leading slash.  Otherwise, this method returns the pattern
     * exactly as specified in the descriptor or Java configuration.</p>
     * 
     * @return the String representation for the
     * {@code url-pattern} for this mapping. 
     * 
     * @since 4.0
     */
    public String getPattern();
    
    /**
     * <p>Return the String representation for the {@code servlet-name}
     * for this mapping.  If the Servlet providing the response is the
     * default servlet, the return from this method is the name of the
     * defautl servlet, which is container specific.</p>
     * 
     * @return the String representation for the {@code servlet-name}
     * for this mapping.
     * 
     * @since 4.0
     */
    public String getServletName();

    /**
     * <p>Return the {@link MappingMatch} for this 
     * instance</p> 
     * 
     * @return the {@code MappingMatch} for this instance.
     * 
     * @since 4.0
     */
    public MappingMatch getMappingMatch();
    
}
