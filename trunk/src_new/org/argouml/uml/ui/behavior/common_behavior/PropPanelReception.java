// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;

import org.argouml.swingext.GridLayout2;

import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLTextArea;
import org.argouml.uml.ui.UMLTextProperty;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @author Jaap
 *
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelReception extends PropPanelModelElement {

    public PropPanelReception() {
        super("Reception", _receptionIcon, ConfigLoader.getTabPropsOrientation());

        Class mclass = (Class) ModelFacade.RECEPTION;

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), getStereotypeBox());
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());

        JPanel modPanel = new JPanel(new GridLayout2(0, 3, GridLayout2.ROWCOLPREFERRED));
        // next line does not contain typing errors, NSUML is not correct (isabstarct instead of isabstract)
        modPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-lc"), this, new UMLReflectionBooleanProperty("isAbstarct", mclass, "isAbstarct", "setAbstarct")));
        modPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-lc"), this, new UMLReflectionBooleanProperty("isLeaf", mclass, "isLeaf", "setLeaf")));
        modPanel.add(new UMLCheckBox(localize("root"), this, new UMLReflectionBooleanProperty("isRoot", mclass, "isRoot", "setRoot")));
        addField(Argo.localize("UMLMenu", "label.modifiers"), modPanel);

        addSeperator();

        addField(Argo.localize("UMLMenu", "label.signal"), new UMLReceptionSignalComboBox(this, new UMLReceptionSignalComboBoxModel()));

        JScrollPane specificationScroll = new JScrollPane(new UMLTextArea(this, new UMLTextProperty(mclass, "specification", "getSpecification" , "setSpecification")), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addField(Argo.localize("UMLMenu", "label.specification"), specificationScroll);

        new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateUp", null);
	new PropPanelButton(this, buttonPanel, _deleteIcon, Argo.localize("UMLMenu", "button.delete-operation"), "removeElement", null);
    }


    /**
     * Returns true if a given modelelement is an acceptable owner of this reception.
     * Only classifiers that are no datatype are acceptable.
     * @param element
     * @return boolean
     */
    public boolean isAcceptibleClassifier(MModelElement element) {
        return (ModelFacade.isAClassifier(element) && !(ModelFacade.isADataType(element)));
    }

    /**
     * Returns the owner of the reception. Necessary for the MClassifierComboBox.
     * @return MClassifier
     */
    public Object getOwner() {
        Object target = getTarget();
        if (ModelFacade.isAReception(target)) {
            return ModelFacade.getOwner(target);
        }
        return null;
    }

    /**
     * Sets the owner of the reception. Necessary for the MClassifierComboBox.
     * @param owner
     */
    public void setOwner(MClassifier owner) {
        Object target = getTarget();
        if (ModelFacade.isAReception(target)) {
            Object rec = /*(MReception)*/ target;
            if (ModelFacade.getOwner(rec) != null) {
                ModelFacade.setOwner(rec, null);
            }
            ModelFacade.setOwner(rec, owner);
        }
    }
}