// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

// File: FigState.java
// Classes: FigState
// Original Author: your email address here
// $Id$

package uci.uml.visual;

import java.awt.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;

import uci.gef.*;
import uci.graph.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

/** Class to display graphics for a UML State in a diagram. */

public class FigState extends FigNode
implements VetoableChangeListener, DelayedVetoableChangeListener {

  ////////////////////////////////////////////////////////////////
  // constants
  
  public final int MARGIN = 2;

  ////////////////////////////////////////////////////////////////
  // instance variables
  
  /** The main label on this icon. */
  FigText _name;
  
  /** UML does not really use ports, so just define one big one so
   *  that users can drag edges to or from any point in the icon. */
  
  FigRect _bigPort;
  
  // add other Figs here aes needed


  ////////////////////////////////////////////////////////////////
  // constructors
  
  public FigState(GraphModel gm, Object node) {
    super(node);
    // if it is a UML meta-model object, register interest in any change events
    if (node instanceof ElementImpl)
      ((ElementImpl)node).addVetoableChangeListener(this);

    Color handleColor = Globals.getPrefs().getHandleColor();
    _bigPort = new FigRect(10, 10, 90, 20, handleColor, Color.lightGray);
    _name = new FigText(10,10,90,20, Color.blue, "Times", 10);
    _name.setExpandOnly(true);
    _name.setText("FigState");
    // initialize any other Figs here

    // add Figs to the FigNode in back-to-front order
    addFig(_bigPort);
    addFig(_name);


    Object onlyPort = node;
    bindPort(onlyPort, _bigPort);
    setBlinkPorts(true); //make port invisble unless mouse enters
    Rectangle r = getBounds();
  }


  /** If the UML meta-model object changes state. Update the Fig.  But
   *  we need to do it as a "DelayedVetoableChangeListener", so that
   *  model changes complete before we update the screen. */
  public void vetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    System.out.println("FigState got a change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
      SwingUtilities.invokeLater(delayedNotify);
    }
  }

  /** The UML meta-model object changed. Now update the Fig to show
   *  its current  state. */
  public void delayedVetoableChange(PropertyChangeEvent pce) {
    // throws PropertyVetoException 
    System.out.println("FigState got a delayed change notification!");
    Object src = pce.getSource();
    if (src == getOwner()) {
      updateText();
      // you may have to update more than just the text
    }
  }


  /** Update the text labels */
  protected void updateText() {
    Element elmt = (Element) getOwner();
    String nameStr = GeneratorDisplay.Generate(elmt.getName());

    startTrans();
    _name.setText(nameStr);
    Rectangle bbox = getBounds();
    setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    endTrans();
  }

  

  public void dispose() {
    if (!(getOwner() instanceof Element)) return;
    Element elmt = (Element) getOwner();
    Project p = ProjectBrowser.TheInstance.getProject();
    p.moveToTrash(elmt);
    super.dispose();
  }



} /* end class FigState */
