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

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;

/** A permanent Mode to catch right-mouse-button events and show a
 *  popup menu.  Needs-more-work: this is not fully implemented
 *  yet. It should ask the Fig under the mouse what menu it should
 *  offer. */

public class ModePopup extends Mode {

  ////////////////////////////////////////////////////////////////
  // static variables
  
  /** A test menu to pop up. */
  static JPopupMenu POP = new JPopupMenu("test");

  ////////////////////////////////////////////////////////////////
  // static initializer
  
  /** Configures test menu */
  static {
    POP.add(new CmdReorder(CmdReorder.SEND_TO_BACK));
    POP.add(new CmdReorder(CmdReorder.BRING_TO_FRONT));
    POP.add(new CmdReorder(CmdReorder.SEND_BACKWARD));
    POP.add(new CmdReorder(CmdReorder.BRING_FORWARD));
  }

  ////////////////////////////////////////////////////////////////
  //  constructor
  
  public ModePopup(Editor par) { super(par); }

  ////////////////////////////////////////////////////////////////
  // accessors
  
  /** Always false because I never want to get out of popup mode. */
  public boolean canExit() { return false; }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Show a popup menu on right-mouse-button up. */
  public void mouseReleased(MouseEvent me) {
    if (me.isPopupTrigger() || me.getModifiers() == InputEvent.BUTTON3_MASK) {
      POP.show(_editor.getAwtComponent(), me.getX(), me.getY());
      me.consume();
    }
  }

} /* end class ModePopup */
