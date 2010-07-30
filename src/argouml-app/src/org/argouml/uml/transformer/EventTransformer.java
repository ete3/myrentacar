/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *******************************************************************************
 */

package org.argouml.uml.transformer;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;

import org.argouml.kernel.Project;
import org.argouml.model.Model;

class EventTransformer implements Transformer {

    public List<Action> actions(Project p, Object sourceModelElement) {
        assert Model.getFacade().isAEvent(sourceModelElement);
        List<Action> result = new ArrayList<Action>();
        if (!Model.getFacade().isACallEvent(sourceModelElement)) {
            result.add(new EventToCallEvent(p, sourceModelElement));
        }
        if (!Model.getFacade().isASignalEvent(sourceModelElement)) {
            result.add(new EventToSignalEvent(p, sourceModelElement));
        }
        if (!Model.getFacade().isAChangeEvent(sourceModelElement)) {
            result.add(new EventToChangeEvent(p, sourceModelElement));
        }
        if (!Model.getFacade().isATimeEvent(sourceModelElement)) {
            result.add(new EventToTimeEvent(p, sourceModelElement));
        }
        return result;
    }

    public boolean canTransform(Object sourceModelElement) {
        return Model.getFacade().isAEvent(sourceModelElement);
    }
    
    abstract class EventTransformerAction extends TransformerAction {
        /**
         * Constructor.
         */
        EventTransformerAction(String nameKey, Project project, Object sourceModelElement) {
            super(nameKey, project, sourceModelElement);
        }
        
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            String text = getText();
            String name = getName();
            Object ns = Model.getFacade().getNamespace(source);
            /* Need to copy the lists since we will be moving the elements 
             * one by one: */
            Collection transitions = new ArrayList(
                    Model.getFacade().getTransitions(source));
            Collection parameters = new ArrayList(
                    Model.getFacade().getParameters(source));
            // Deleting "source" here would probably delete the parameters...

            Object trans = null;
            if (!transitions.isEmpty()) {
                trans = transitions.iterator().next();
            }
            Object evt = buildEvent(text, name, ns, trans);
            
            for (Object param : parameters) {
                Model.getCoreHelper().removeParameter(source, param);
                Model.getCoreHelper().addParameter(evt, param);
            }
            for (Object t : transitions) {
                Model.getStateMachinesHelper().setTrigger(t, null);
                Model.getStateMachinesHelper().setTrigger(t, evt);
            }

            Model.getUmlFactory().delete(source);
        }

        protected String getText() {
            String text = "";
            if (Model.getFacade().isACallEvent(source)) {
                text = Model.getFacade().getName(source);
            }
            if (Model.getFacade().isAChangeEvent(source)) {
                Object changeExpr = Model.getFacade().getChangeExpression(source);
                if (changeExpr != null) {
                    text = Model.getDataTypesHelper().getBody(changeExpr);
                }
            }
            if (Model.getFacade().isATimeEvent(source)) {
                Object timeExpr =  Model.getFacade().getWhen(source);
                if (timeExpr != null) {
                    text = Model.getDataTypesHelper().getBody(timeExpr);
                }
            }
            if (Model.getFacade().isASignalEvent(source)) {
                text = Model.getFacade().getName(source);
            }
            return text;
        }
        
        /*
         * Only set the name if it differs from the text.
         */
        protected String getName() {
            String name = "";
            if (Model.getFacade().isAChangeEvent(source)) {
                name = Model.getFacade().getName(source);
            }
            if (Model.getFacade().isATimeEvent(source)) {
                name = Model.getFacade().getName(source);
            }
            return name;
        }

        abstract protected Object buildEvent(String text, String name, Object ns, Object trans);
    }

    class EventToCallEvent extends EventTransformerAction {

        /**
         * Constructor.
         */
        EventToCallEvent(Project project, Object sourceModelElement) {
            super("button.new-callevent", project, sourceModelElement);
        }

        @Override
        protected Object buildEvent(String text, String name, Object ns, Object trans) {
            Object evt = Model.getStateMachinesFactory().buildCallEvent(
                    ns);
            Model.getCoreHelper().setName(evt, text);
            Object op = Model.getStateMachinesHelper().findOperationByName(
                    trans, text);
            if (op != null) {
                Model.getCommonBehaviorHelper().setOperation(evt, op);
            }
            return evt;
        }
    }
    
    class EventToSignalEvent extends EventTransformerAction {

        /**
         * Constructor.
         */
        EventToSignalEvent(Project project, Object sourceModelElement) {
            super("button.new-signalevent", project, sourceModelElement);
        }

        @Override
        protected Object buildEvent(String text, String name, Object ns, Object trans) {
            return Model.getStateMachinesFactory().buildSignalEvent(
                    text, ns);
        }
    }

    class EventToChangeEvent extends EventTransformerAction {

        /**
         * Constructor.
         */
       EventToChangeEvent(Project project, Object sourceModelElement) {
            super("button.new-changeevent", project, sourceModelElement);
        }

        @Override
        protected Object buildEvent(String text, String name, Object ns, Object trans) {
            Object evt =  Model.getStateMachinesFactory().buildChangeEvent(
                    text, ns);
            Model.getCoreHelper().setName(evt, name);
            return evt;
        }
    }
    
    class EventToTimeEvent extends EventTransformerAction {

        /**
         * Constructor.
         */
       EventToTimeEvent(Project project, Object sourceModelElement) {
            super("button.new-timeevent", project, sourceModelElement);
        }

        @Override
        protected Object buildEvent(String text, String name, Object ns, Object trans) {
            Object evt =  Model.getStateMachinesFactory().buildTimeEvent(
                    text, ns);
            Model.getCoreHelper().setName(evt, name);
            return evt;
        }
    }

}
