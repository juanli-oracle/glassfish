/*
 * Copyright � 2003 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright � 2003 Sun Microsystems, Inc. Tous droits r�serv�s.
 *
 * Droits du gouvernement am�ricain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * suppl�ments � celles-ci.  Distribu� par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants d�velopp�s par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques d�pos�es de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
 */


package dataregistry;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.*;


public interface LocalPartHome extends EJBLocalHome {

    public LocalPart findByPrimaryKey(PartKey aKey)
            throws FinderException;

    public LocalPart create(String partNumber, int revision, String description,
            Date revisionDate, String specification, Serializable drawing)
            throws CreateException;

}
