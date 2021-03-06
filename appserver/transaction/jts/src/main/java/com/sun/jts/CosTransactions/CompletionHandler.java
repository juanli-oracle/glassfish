/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

//----------------------------------------------------------------------------
//
// Module:      CompletionHandler.java
//
// Description: Common interface for transaction completion objects.
//
// Product:     com.sun.jts.CosTransactions
//
// Author:      Simon Holdsworth
//
// Date:        March, 1997
//
// Copyright (c):   1995-1997 IBM Corp.
//
//   The source code for this program is not published or otherwise divested
//   of its trade secrets, irrespective of what has been deposited with the
//   U.S. Copyright Office.
//
//   This software contains confidential and proprietary information of
//   IBM Corp.
//----------------------------------------------------------------------------

package com.sun.jts.CosTransactions;

//------------------------------------------------------------------------------
// CompletionHandler interface
//------------------------------------------------------------------------------
/**The CompletionHandler interface provides operations that allow an object
 * to be informed when a Coordinator locally completes a transaction.
 * <p>
 * This is to allow the CoordinatorResource and CoordinatorTerm objects
 * for a transaction to be informed when the Coordinator is completed via
 * some path other than normal.
 *
 * @version 0.1
 *
 * @author Simon Holdsworth, IBM Corporation
 *
 * @see
 */
// CHANGE HISTORY
//
// Version By     Change Description
//   0.1   SAJH   Initial implementation.
//------------------------------------------------------------------------------

interface CompletionHandler {

    /**Informs the ComplemtionHandler object that the transaction it represents
     * has completed.
     * <p>
     * Flags indicate whether the transaction aborted, and whether there was
     * heuristic damage.
     * <p>
     * This operation is invoked by a Coordinator when it is rolled back,
     * potentially by a caller other than the CompletionHandler itself.
     *
     * @param aborted          Indicates whether the transaction locally aborted.
     * @param heuristicDamage  Indicates local heuristic damage.
     *
     * @return
     *
     * @see
     */
    abstract void setCompleted( boolean aborted,
                                boolean heuristicDamage );
}
