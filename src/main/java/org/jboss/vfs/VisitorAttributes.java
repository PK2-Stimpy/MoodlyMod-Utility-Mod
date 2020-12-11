/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag.
*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package org.jboss.vfs;

import static org.jboss.vfs.VFSMessages.MESSAGES;

/**
 * Attributes used when visiting a virtual file system
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class VisitorAttributes {

    /**
     * A VirtualFileFilter than accepts any file
     */
    public static final AcceptAnyFilter RECURSE_ALL = new AcceptAnyFilter();

    /**
     * The default attributes - visit leaves and non-leaves, no recursion, no root
     */
    public static final VisitorAttributes DEFAULT = new ImmutableVisitorAttributes();

    /**
     * Visit leaves only and do not recurse non-leaf files
     */
    public static final VisitorAttributes LEAVES_ONLY = new ImmutableVisitorAttributes(true, null);

    /**
     * Recurse and visit all non-leaf files
     */
    public static final VisitorAttributes RECURSE = new ImmutableVisitorAttributes(false, RECURSE_ALL);

    /**
     * Recurse all non-leaf files but only visit leaves
     */
    public static final VisitorAttributes RECURSE_LEAVES_ONLY = new ImmutableVisitorAttributes(true, RECURSE_ALL);

    /**
     * Whether to include the root
     */
    private boolean includeRoot;

    /**
     * Whether to only visit leaves
     */
    private boolean leavesOnly;

    /**
     * Whether to ignore individual file errors
     */
    private boolean ignoreErrors;

    /**
     * Whether to include hidden files
     */
    private boolean includeHidden;

    /**
     * A filter used to control whether a non-leaf is recursive visited
     */
    private VirtualFileFilter recurseFilter;

    /**
     * Whether to visit leaves only<p>
     * <p/>
     * Default: false
     *
     * @return the visit leaves only.
     */
    public boolean isLeavesOnly() {
        return leavesOnly;
    }

    /**
     * Set the leaves only.
     *
     * @param leavesOnly the leaves only
     * @throws IllegalStateException if you attempt to modify one of the preconfigured static values of this class
     */
    public void setLeavesOnly(boolean leavesOnly) {
        this.leavesOnly = leavesOnly;
    }

    /**
     * Whether to recurse into the non-leaf file<p>. If there is a recurse filter then the result will by its
     * accepts(file) value.
     * <p/>
     * Default: false
     *
     * @param file the file
     * @return the recurse flag.
     */
    public boolean isRecurse(VirtualFile file) {
        boolean recurse = false;
        if (recurseFilter != null) { recurse = recurseFilter.accepts(file); }
        return recurse;
    }

    /**
     * Get the recurse filter.
     *
     * @return the current recurse filter.
     */
    public VirtualFileFilter getRecurseFilter() {
        return recurseFilter;
    }

    /**
     * Set the recurse filter.
     *
     * @param filter - the recurse filter.
     * @throws IllegalStateException if you attempt to modify one of the preconfigured static values of this class
     */
    public void setRecurseFilter(VirtualFileFilter filter) {
        recurseFilter = filter;
    }

    /**
     * Whether to include the root of the visit<p>
     * <p/>
     * Default: false
     *
     * @return the includeRoot.
     */
    public boolean isIncludeRoot() {
        return includeRoot;
    }

    /**
     * Set the includeRoot.
     *
     * @param includeRoot the includeRoot.
     * @throws IllegalStateException if you attempt to modify one of the preconfigured static values of this class
     */
    public void setIncludeRoot(boolean includeRoot) {
        this.includeRoot = includeRoot;
    }

    /**
     * Whether to ignore individual errors<p>
     * <p/>
     * Default: false
     *
     * @return the ignoreErrors.
     */
    public boolean isIgnoreErrors() {
        return ignoreErrors;
    }

    /**
     * Set the ignoreErrors.
     *
     * @param ignoreErrors the ignoreErrors.
     * @throws IllegalStateException if you attempt to modify one of the preconfigured static values of this class
     */
    public void setIgnoreErrors(boolean ignoreErrors) {
        this.ignoreErrors = ignoreErrors;
    }

    /**
     * Whether to include hidden files<p>
     * <p/>
     * Default: false
     *
     * @return the includeHidden.
     */
    public boolean isIncludeHidden() {
        return includeHidden;
    }

    /**
     * Set the includeHidden.
     *
     * @param includeHidden the includeHidden.
     * @throws IllegalStateException if you attempt to modify one of the preconfigured static values of this class
     */
    public void setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
    }

    private static class AcceptAnyFilter implements VirtualFileFilter {

        public boolean accepts(VirtualFile file) {
            return true;
        }
    }

    /**
     * Immutable version of the attribues
     */
    private static class ImmutableVisitorAttributes extends VisitorAttributes {

        /**
         * Create a new ImmutableVirtualFileVisitorAttributes with default values
         */
        ImmutableVisitorAttributes() {
        }

        /**
         * Create a new ImmutableVirtualFileVisitorAttributes.
         *
         * @param leavesOnly    whether to visit leaves only
         * @param recurseFilter - filter which controls whether to recurse
         */
        ImmutableVisitorAttributes(boolean leavesOnly, VirtualFileFilter recurseFilter) {
            super.setLeavesOnly(leavesOnly);
            super.setRecurseFilter(recurseFilter);
        }

        @Override
        public void setLeavesOnly(boolean leavesOnly) {
            throw MESSAGES.preconfiguredAttributesAreImmutable();
        }

        @Override
        public void setIncludeRoot(boolean includeRoot) {
            throw MESSAGES.preconfiguredAttributesAreImmutable();
        }

        @Override
        public void setRecurseFilter(VirtualFileFilter filter) {
            throw MESSAGES.preconfiguredAttributesAreImmutable();
        }

        @Override
        public void setIgnoreErrors(boolean ignoreErrors) {
            throw MESSAGES.preconfiguredAttributesAreImmutable();
        }

        @Override
        public void setIncludeHidden(boolean includeHidden) {
            throw MESSAGES.preconfiguredAttributesAreImmutable();
        }
    }
}
