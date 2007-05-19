/* Copyright (C) 2004-2005 by Peter Eastman

   This program is free software; you can redistribute it and/or modify it under the
   terms of the GNU General Public License as published by the Free Software
   Foundation; either version 2 of the License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful, but WITHOUT ANY 
   WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
   PARTICULAR PURPOSE.  See the GNU General Public License for more details. */

package artofillusion.ui;

import artofillusion.*;
import buoy.widget.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * This class provides a variety of static methods for performing useful UI related operations.
 */

public class UIUtilities
{
  
  /** Given a Window, center it in the screen. */
  
  public static void centerWindow(WindowWidget win)
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(win.getComponent().getGraphicsConfiguration());
    screenSize.width -= screenInsets.left+screenInsets.right;
    screenSize.height -= screenInsets.top+screenInsets.bottom;
    Rectangle winBounds = win.getBounds();
    int x = (screenSize.width-winBounds.width)/2;
    int y = (screenSize.height-winBounds.height)/2;
    if (x < screenInsets.left)
      x = screenInsets.left;
    if (y < screenInsets.top)
      y = screenInsets.top;
    win.setBounds(new Rectangle(x, y, winBounds.width, winBounds.height));
  }

  /** Given a BDialog, center it relative to a parent window. */
  
  public static void centerDialog(BDialog dlg, WindowWidget parent)
  {
    Rectangle r1 = parent.getBounds(), r2 = dlg.getBounds();
    int x = r1.x+(r1.width-r2.width)/2;
    int y = r1.y+20;
    if (x < 0)
      x = 0;
    if (y < 0)
      y = 0;
    dlg.setBounds(new Rectangle(x, y, r2.width, r2.height));
  }

  
  /** Set up a Widget and all of its children to have the default font for the program. */
  
  public static void applyDefaultFont(Widget w)
  {
    if (ModellingApp.defaultFont == null)
      return;
    w.setFont(ModellingApp.defaultFont);
    if (w instanceof WidgetContainer && !(w instanceof BMenuBar))
    {
      Iterator children = ((WidgetContainer) w).getChildren().iterator();
      while (children.hasNext())
        applyDefaultFont((Widget) children.next());
    }
  }
  
  /** Set up a Widget and all of its children to have the default background color for the program. */
  
  public static void applyDefaultBackground(Widget w)
  {
    applyBackground(w, ModellingApp.APP_BACKGROUND_COLOR);
  }

  /** Set up a Widget and all of its children to have a specific background color. */
  
  public static void applyBackground(Widget w, Color color)
  {
    if (w instanceof WidgetContainer)
    {
      w.setBackground(color);
      Iterator children = ((WidgetContainer) w).getChildren().iterator();
      while (children.hasNext())
        applyBackground((Widget) children.next(), color);
    }
    else if (w instanceof BLabel)
      w.setBackground(color);
    else if (w instanceof BButton || w instanceof BComboBox || w instanceof BCheckBox || w instanceof BRadioButton)
      ((JComponent) w.getComponent()).setOpaque(false);
  }
  
  /** Given an BList, create an appropriate container for it.  This involves a properly configured
      BScrollPane, with an outline around it. */
  
  public static WidgetContainer createScrollingList(BList list)
  {
    BScrollPane scroll = new BScrollPane(list, BScrollPane.SCROLLBAR_AS_NEEDED, BScrollPane.SCROLLBAR_ALWAYS);
    scroll.setBackground(list.getBackground());
    scroll.setForceWidth(true);
    return BOutline.createBevelBorder(scroll, false);
  }
  
  /** Given a Widget, find the window that contains it.  If the Widget is not in a window, return null. */
  
  public static WindowWidget findWindow(Widget w)
  {
    if (w instanceof WindowWidget)
      return (WindowWidget) w;
    if (w == null)
      return null;
    return findWindow(w.getParent());
  }
  
  /** Given a Widget, find its parent BFrame.  If the Widget is inside a BFrame, that frame will be
      returned.  If it is inside a BDialog, this returns the dialog's parent frame.  Otherwise, 
      this returns null. */
  
  public static BFrame findFrame(Widget w)
  {
    if (w instanceof BFrame)
      return (BFrame) w;
    if (w == null)
      return null;
    return findFrame(w.getParent());
  }
  
  /** Break a string into lines which are short enough to easily display in a window. */
  
  public static String [] breakString(String s)
  {
    int lines = (s.length()/60)+1;
    if (lines < 2)
      return new String [] {s};
    int lineLength = s.length()/lines;
    Vector line = new Vector();
    int index = 0;
    while (index+lineLength < s.length())
    {
      int next = s.indexOf(' ', index+lineLength);
      if (next == -1)
        next = s.length();
      line.addElement(s.substring(index, next).trim());
      index = next;
    }
    if (index < s.length())
      line.addElement(s.substring(index).trim());
    String result[] = new String [line.size()];
    line.copyInto(result);
    return result;
  }

  /** Recursively enable or disable a container and everything inside it. */

  public static void setEnabled(Widget w, boolean enabled)
  {
    w.setEnabled(enabled);
    if (w instanceof WidgetContainer)
    {
      Iterator children = ((WidgetContainer) w).getChildren().iterator();
      while (children.hasNext())
        setEnabled((Widget) children.next(), enabled);
    }
  }
}
