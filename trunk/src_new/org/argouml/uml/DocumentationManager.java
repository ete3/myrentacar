// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.ModelFacade;
import org.argouml.util.MyTokenizer;

public class DocumentationManager {

    /** The system's native line-ends, for when things are written to file */
    private static final String LINE_SEPARATOR =
	System.getProperty("line.separator");

    public static String getDocs(Object o, String indent) {
        return getDocs(o, indent, "/** ", " *  ", " */");
    }

    public static String getDocs(Object o, String indent, String header,
				 String prefix, String footer) {
        String sResult = defaultFor(o, indent);

        if (ModelFacade.isAModelElement(o)) {
            Iterator iter = ModelFacade.getTaggedValues(o);
            if (iter != null) {
                while (iter.hasNext()) {
                    Object tv = iter.next();
                    String tag = ModelFacade.getTagOfTag(tv);
                    if (tag.equals("documentation") || tag.equals("javadocs")) {
                        sResult = ModelFacade.getValueOfTag(tv);
                        // give priority to "documentation"
                        if (tag.equals("documentation")) break;
                    }
                }
            }
        }

        if (sResult == null)
            return "(No comment)";

        StringBuffer result = new StringBuffer();
        if (header != null)
            result.append(header).append('\n');
        result.append(sResult);

        // Let every line start with a prefix.
        if (prefix != null) {
            for (int i = 0; i < result.length() - 1; i++) {
                if (result.charAt(i) == '\n') {
                    result.insert(i + 1, prefix);
                    if (indent != null)
                        result.insert(i + 1, indent);
                }
            }
        }

        if (footer != null) {
            result.append('\n').append(indent).append(footer);
        }
        return result.toString();
    }

    public static void setDocs(Object o, String s) {
        ModelFacade.setTaggedValue(o, "documentation", s);
    }

    /**
     * Determine whether documentation is associated with the given
     * element or not.
     *
     * Added 2001-10-05 STEFFEN ZSCHALER for use by
     * org.argouml.language.java.generator.CodeGenerator
     *
     * @param o The given element.
     * @return true if the given element has docs.
     */
    public static boolean hasDocs(Object o) {
	if (ModelFacade.isAModelElement(o)) {
	    Iterator i = ModelFacade.getTaggedValues(o);

	    if (i != null) {
		while (i.hasNext()) {
		    Object tv = i.next();
		    String tag = ModelFacade.getTagOfTag(tv);
		    String value = ModelFacade.getValueOfTag(tv);
		    if ((tag.equals("documentation") || tag.equals("javadocs"))
			&& value != null && value.trim().length() > 0) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    ////////////////////////////////////////////////////////////////
    // default documentation

    public static String defaultFor(Object o, String indent) {
	if (ModelFacade.isAClass(o)) {
	    /*
	     * Changed 2001-10-05 STEFFEN ZSCHALER
	     *
	     * Was (space added below!):
	     *
	     return
	     "/** A class that represents ...\n"+
	     " * \n"+
	     " * @see OtherClasses\n"+
	     " * @author your_name_here\n"+
	     " * /";
	     *
	     */
	    return " A class that represents ...\n\n"
		+ indent + " @see OtherClasses\n"
		+ indent + " @author your_name_here";
	}
	if (ModelFacade.isAAttribute(o)) {
	    /*
	     * Changed 2001-10-05 STEFFEN ZSCHALER
	     *
	     * Was (space added below!):
	     *
	     return
	     "/** An attribute that represents ...\n"+
	     " * /";
	     *
	     */
	    return " An attribute that represents ...";
	}

	if (ModelFacade.isAOperation(o)) {
	    /*
	     * Changed 2001-10-05 STEFFEN ZSCHALER
	     *
	     * Was (space added below!):
	     *
	     return
	     "/** An operation that does ...\n"+
	     " * \n"+
	     " * @param firstParamName  a description of this parameter\n"+
	     " * /";
	     *
	     */
	    return " An operation that does...\n\n"
		+ indent + " @param firstParam a description of this parameter";
	}
	if (ModelFacade.isAInterface(o)) {
	    /*
	     * Changed 2001-10-05 STEFFEN ZSCHALER
	     *
	     * Was (space added below!):
	     *
	     return
	     "/** A interface defining operations expected of ...\n"+
	     " * \n"+
	     " * @see OtherClasses\n"+
	     " * @author your_name_here\n"+
	     " * /";
	     *
	     */
	    return " A interface defining operations expected of ...\n\n"
		+ indent + " @see OtherClasses\n"
		+ indent + " @author your_name_here";
	}
	if (ModelFacade.isAModelElement(o)) {
	    /*
	     * Changed 2001-10-05 STEFFEN ZSCHALER
	     *
	     * Was (space added below!):
	     *
	     return
	     "/**\n"+
	     " * \n"+
	     " * /";
	     *
	     */
	    return "\n";
	}

	/*
	 * Changed 2001-10-05 STEFFEN ZSCHALER
	 *
	 * Was:
	 return "(No documentation)";
	 *
	 */
	return null;
    }


    ////////////////////////////////////////////////////////////////
    // comments

    /**
     * Get the comments (the notes in a diagram) for a modelelement.<p>
     *
     * This returns a c-style comments.
     *
     * @param o The modelelement.
     * @return a String.
     */
    public static String getComments(Object o) {
        return getComments(o, "/*", " * ", " */");
    }

    /**
     * Get the comments (the notes in a diagram) for a modelelement.
     *
     * @return a string with the comments.
     * @param o The given modelelement.
     * @param header is the comment header.
     * @param prefix is the comment prefix (on every line).
     * @param footer is the comment footer.
     */
    public static String getComments(Object o,
				     String header, String prefix,
				     String footer) {
	StringBuffer result = new StringBuffer();
	if (header != null) {
	    result.append(header).append(LINE_SEPARATOR);
	}

	if (ModelFacade.isAModelElement(o)) {
	    Collection comments = ModelFacade.getComments(o);
	    if (!comments.isEmpty()) {
		for (Iterator iter = comments.iterator(); iter.hasNext(); ) {
		    Object c = iter.next();
		    String s = ModelFacade.getName(c);
		    appendComment(result, prefix, s);
		}
	    } else {
		return "";
	    }
	} else {
	    return "";
	}

	if (footer != null) {
	    result.append(footer).append(LINE_SEPARATOR);
	}

	return result.toString();
    }

    /**
     * Append a string to sb which is chopped into lines and each line
     * prefixed with prefix.
     */
    private static void appendComment(StringBuffer sb, String prefix,
				      String comment) {
	if (comment == null) {
	    return;
	}

	MyTokenizer tokens = new MyTokenizer(comment,
					     "",
					     MyTokenizer.LINE_SEPARATOR);

	while (tokens.hasMoreTokens()) {
	    String s = tokens.nextToken();
	    if (!s.startsWith("\r") && !s.startsWith("\n")) {
		sb.append(prefix);
		sb.append(s);
		sb.append(LINE_SEPARATOR);
	    }
	}
    }

} /* end class DocumentationManager */




