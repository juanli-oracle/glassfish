/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
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
package com.sun.enterprise.v3.admin;

import com.sun.enterprise.module.bootstrap.StartupContext;
import java.util.logging.*;
import org.glassfish.api.*;
import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.admin.Cluster;
import org.glassfish.api.admin.RuntimeType;
import org.jvnet.hk2.annotations.*;
import org.jvnet.hk2.component.*;
import static org.glassfish.api.ActionReport.ExitCode.SUCCESS;
import static org.glassfish.api.ActionReport.ExitCode.FAILURE;

/**
 * https://glassfish.dev.java.net/issues/show_bug.cgi?id=12483
 * @author Byron Nevins
 */
@Service(name = "_get-runtime-info")
@Scoped(PerLookup.class)
public class RuntimeInfo implements AdminCommand {

    public RuntimeInfo() {
    }

    @Override
    public void execute(AdminCommandContext context) {
        report = context.getActionReport();
        report.setActionExitCode(SUCCESS);
        top = report.getTopMessagePart();
        logger = context.getLogger();
        jpdaEnabled = Boolean.parseBoolean(ctx.getArguments().getProperty("-debug"));
        debug();
        report.setMessage(reportMessage.toString());
    }

    private void debug() {
        top.addProperty("debug", Boolean.toString(jpdaEnabled));
        reportMessage.append(Strings.get("runtime.info.debug", jpdaEnabled ? "enabled" : "not enabled"));
    }
    @Inject
    StartupContext ctx;
    @Param(optional = true)
    String target;
    private boolean jpdaEnabled;
    private ActionReport report;
    private ActionReport.MessagePart top;
    private Logger logger;
    private StringBuilder reportMessage = new StringBuilder();
}
