/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
package org.glassfish.connectors.admin.cli;

import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.ActionReport;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;
import org.jvnet.hk2.config.ConfigSupport;
import org.jvnet.hk2.config.SingleConfigCode;
import org.jvnet.hk2.config.TransactionFailure;
import com.sun.enterprise.config.serverbeans.GroupMap;
import com.sun.enterprise.config.serverbeans.PrincipalMap;
import com.sun.enterprise.config.serverbeans.Resource;
import com.sun.enterprise.config.serverbeans.Resources;
import com.sun.enterprise.config.serverbeans.WorkSecurityMap;
import com.sun.enterprise.util.LocalStringManagerImpl;

import java.beans.PropertyVetoException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create Connector Work Security Map
 *
 */
@Service(name="create-connector-work-security-map")
@Scoped(PerLookup.class)
@I18n("create.connector.work.security.map")
public class CreateConnectorWorkSecurityMap implements AdminCommand {

    final private static LocalStringManagerImpl localStrings =
            new LocalStringManagerImpl(CreateConnectorWorkSecurityMap.class);

    @Param(name="raname")
    String raName;

    @Param(name="principalsmap", optional=true, separator=':')
    Properties principalsMap;

    @Param(name = "groupsmap", optional=true, separator=':')
    Properties groupsMap;

    @Param(name="description", optional=true)
    String description;

    @Param(name="mapname", primary=true)
    String mapName;

    @Inject
    Resources resources;

    /**
     * Executes the command with the command parameters passed as Properties
     * where the keys are the paramter names and the values the parameter values
     *
     * @param context information
     */
    public void execute(AdminCommandContext context) {
        final ActionReport report = context.getActionReport();

        if (mapName == null) {
            report.setMessage(localStrings.getLocalString(
                    "create.connector.work.security.map.noMapName",
                    "No mapname defined for connector work security map."));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

        if (raName == null) {
            report.setMessage(localStrings.getLocalString(
                    "create.connector.work.security.map.noRaName",
                    "No raname defined for connector work security map."));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

        if (principalsMap == null && groupsMap == null) {
            report.setMessage(localStrings.getLocalString(
                    "create.connector.work.security.map.noMap",
                    "No principalsmap or groupsmap defined for connector work security map."));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

        if (principalsMap != null && groupsMap != null) {
            report.setMessage(localStrings.getLocalString(
                    "create.connector.work.security.map.specifyPrincipalsOrGroupsMap",
                    "A work-security-map can have either (any number of) group mapping  " +
                    "or (any number of) principals mapping but not both. Specify" +
                    "--principalsmap or --groupsmap."));
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            return;
        }

        // ensure we don't already have one of this name
        for (Resource resource : resources.getResources()) {
            if (resource instanceof WorkSecurityMap) {
                if (((WorkSecurityMap) resource).getName().equals(mapName) &&
                        ((WorkSecurityMap) resource).getResourceAdapterName().equals(raName))
                {
                    report.setMessage(localStrings.getLocalString(
                            "create.connector.work.security.map.duplicate",
                            "A connector work security map named {0} for resource adapter {1} already exists.",
                            mapName, raName));
                    report.setActionExitCode(ActionReport.ExitCode.FAILURE);
                    return;
                }
            }
        }

        try {
            ConfigSupport.apply(new SingleConfigCode<Resources>() {

                public Object run(Resources param) throws PropertyVetoException,
                        TransactionFailure {

                    WorkSecurityMap workSecurityMap =
                            param.createChild(WorkSecurityMap.class);
                    workSecurityMap.setName(mapName);
                    workSecurityMap.setResourceAdapterName(raName);

                    if (principalsMap != null) {
                        for (Map.Entry e : principalsMap.entrySet()) {
                            PrincipalMap principalMap = workSecurityMap.createChild(PrincipalMap.class);
                            principalMap.setEisPrincipal((String)e.getKey());
                            principalMap.setMappedPrincipal((String)e.getValue());
                            workSecurityMap.getPrincipalMap().add(principalMap);
                        }
                    } else if (groupsMap != null) {
                        for (Map.Entry e : groupsMap.entrySet()) {
                            GroupMap groupMap = workSecurityMap.createChild(GroupMap.class);
                            groupMap.setEisGroup((String)e.getKey());
                            groupMap.setMappedGroup((String)e.getValue());
                            workSecurityMap.getGroupMap().add(groupMap);
                        }
                    } else {
                        // no mapping
                    }

                    param.getResources().add(workSecurityMap);
                    return workSecurityMap;
                }
            }, resources);
            
        } catch (TransactionFailure tfe) {
            Logger.getLogger(CreateConnectorWorkSecurityMap.class.getName()).log(Level.SEVERE,
                    "create-connector-work-security-map failed", tfe);
            report.setMessage(localStrings.getLocalString(
                    "create.connector.work.security.map.fail",
                    "Unable to create connector work security map {0}.", mapName) +
                    " " + tfe.getLocalizedMessage());
            report.setActionExitCode(ActionReport.ExitCode.FAILURE);
            report.setFailureCause(tfe);
            return;
        }
        
        report.setActionExitCode(ActionReport.ExitCode.SUCCESS);
    }
}
