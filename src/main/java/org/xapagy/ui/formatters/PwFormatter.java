package org.xapagy.ui.formatters;

import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyhtml.ColorCode;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * Extension of the HtmlFormatter which implements the specific GUI elements of
 * the Xapagy WebGui
 * 
 * @author Ladislau Boloni
 * 
 */
public class PwFormatter extends HtmlFormatter implements IXwFormatter {

    public static final String CLASS_BODYLINK = "bodylink";
    private static final String CLASS_EMBED = "embed";
    private static final String CLASS_ENUM = "enum";
    private static final String CLASS_EXPLANATION = "explanation";
    public static final String CLASS_GRAYOUT = "grayout";
    private static final String CLASS_INDENT = "indent";
    public static final String CLASS_LABEL = "label";
    private static final String CLASS_METER_TEXT = "meter-text";
    private static final String CLASS_METER_VALUE = "meter-value";
    private static final String CLASS_METER_VALUE_NEGATIVE =
            "meter-value-negative";

    private static final String CLASS_METER_WRAP = "meter-wrap";
    private static final String CLASS_METER_WRAP_NEGATIVE =
            "meter-wrap-negative";
    public static final String CLASS_NAVBAR = "navbar";
    private static final String CLASS_NOTIFICATION = "notification";
    public static final String CLASS_XAPAGYIDENTIFIER = "xapagyidentifier";

    public PwFormatter() {
        super();
    }

    /**
     * @param string
     */
    public PwFormatter(String string) {
        super(string);
    }

    /**
     * Adds an arrow
     * 
     */
    public String addArrow() {
        add("<span style=\"font-size:200%;\">&rarr;</span>");
        return toString();
    }

    /**
     * A bolded element
     * 
     * @param label
     */
    @Override
    public String addBold(String label) {
        add("<b>" + label + "</b>");
        return toString();
    }

    /**
     * Adds a color code to document the different story lines. The idea is that instances
     * marked with the same color code will be coming from the same story line
     * 
     */
    @Override
    public String addColorCode(ColorCode cc) {
        String style =
                "style=\"display:inline-block;margin:0 0 0 0;border:0px;padding:0px;background-color:#";
        String styleText1 =
                style
                        + Integer.toHexString(cc.getColorFirst().getRGB())
                                .substring(2) + "\"";
        // case 1: a single color over 6 levels
        if (cc.getColorSecond() == null) {
            add("<span " + styleText1 + ">" + "______" + "</span>");
            return toString();
        }
        String styleText2 =
                style
                        + Integer.toHexString(cc.getColorSecond().getRGB())
                                .substring(2) + "\"";
        // case 2: two colors
        if (cc.getColorThird() == null) {
            add("<span " + styleText1 + ">" + "___" + "</span>" + "<span "
                    + styleText2 + ">" + "___" + "</span>");
            return toString();
        }
        String styleText3 =
                style
                        + Integer.toHexString(cc.getColorThird().getRGB())
                                .substring(2) + "\"";
        // three blocks of different colors
        add("<span " + styleText1 + ">" + "__" + "</span>" + "<span "
                + styleText2 + ">" + "__" + "</span>" + "<span " + styleText3
                + ">" + "__" + "</span>");
        return toString();
    }

    /**
     * Adds an enum (monospaced) for types etc
     */
    public String addEnum(String text) {
        openSpan("class=" + PwFormatter.CLASS_ENUM);
        add(text);
        closeSpan();
        return toString();
    }

    /***
     * Adds an error message
     * 
     * @param notificationString
     */
    @Override
    public void addErrorMessage(String notificationString) {
        addPre(notificationString, "class=" + PwFormatter.CLASS_NOTIFICATION);
    }

    /**
     * Adds an extensible component where the component is initially invisible
     * 
     * @param id
     * @param showString
     * @param hideString
     * @param content
     */
    public String addExtensible(String id, String showString,
            String hideString, String content, boolean initiallyVisible) {
        // the show link
        if (initiallyVisible) {
            openA("href=\"#\"", "id=\"" + id + "-show\"", "class=\"more\"",
                    "onclick=\"showHide('" + id + "');return false;\"",
                    "style=\"display:none;\"");
        } else {
            openA("href=\"#\"", "id=\"" + id + "-show\"",
                    "onclick=\"showHide('" + id + "');return false;\"");
        }
        add(showString);
        closeA();
        // the show / hide content
        if (initiallyVisible) {
            openDiv("id=\"" + id + "\"");
        } else {
            openDiv("class=\"more\"", "id=\"" + id + "\"");
        }
        // the hide link
        openA("href=\"#\"", "id=\"" + id + "-hide\"", "onclick=\"showHide('"
                + id + "');return false;\"");
        add(hideString);
        closeA();
        // a little hack to avoid indenting preformatted data
        if (content.contains("<pre")) {
            addNonIndented(content);
        } else {
            addIndented(content);
        }
        closeDiv();
        return toString();
    }

    /**
     * A version of the extensible where the extensible is inside
     * 
     * @param id
     * @param showString
     * @param hideString
     * @param content
     */
    @Override
    public String addExtensibleH2(String id, String label, String content,
            boolean initiallyVisible) {
        String showString =
                "<h2 class=extensible>&gt;&gt;&gt; " + label + "</h2>";
        String hideString =
                "<h2 class=extensible>&lt;&lt;&lt; " + label + "</h2>";
        addExtensible(id, showString, hideString, content, initiallyVisible);
        return toString();
    }

    /**
     * Add a H3 header which is extensible
     * 
     * @param id
     * @param label
     * @param content
     * @param initiallyVisible
     * @return
     */
    @Override
    public String addExtensibleH3(String id, String label, String content,
            boolean initiallyVisible) {
        String showString =
                "<h3 class=extensible>&gt;&gt;&gt; " + label + "</h3>";
        String hideString =
                "<h3 class=extensible>&lt;&lt;&lt; " + label + "</h3>";
        addExtensible(id, showString, hideString, content, initiallyVisible);
        return toString();
    }

    /**
     * Adds an identifier
     */
    @Override
    public String addIdentifier(XapagyComponent xc) {
        openSpan("class=" + PwFormatter.CLASS_XAPAGYIDENTIFIER);
        add(xc.getIdentifier());
        closeSpan();
        return toString();
    }

    /**
     * Frequently found element: a label, followed by a value (or values)
     * 
     * @param fmt
     * @param label
     * @param values
     */
    @Override
    public String addLabelParagraph(String label, String... values) {
        openP();
        openSpan("class=" + PwFormatter.CLASS_LABEL);
        add(label);
        closeSpan();
        for (String value : values) {
            add(value);
        }
        closeP();
        return toString();
    }

    /**
     * Ends an indented part
     */
    @Override
    public void deindent() {
        closeDiv();
    }

    /**
     * Ends an embedded part
     */
    @Override
    public String endEmbed() {
        closeDiv();
        return toString();
    }

    /**
     * Ends an embedded part
     */
    @Override
    public String endEmbedX() {
        closeTD();
        closeTR();
        closeTable();
        closeDiv();
        return toString();
    }

    /**
     * Adds an explanatory note. Frequently describes the format of the
     * following code.
     * 
     * @param explanation
     */
    @Override
    public String explanatoryNote(String explanation) {
        openP("class=" + PwFormatter.CLASS_EXPLANATION);
        add(explanation);
        closeP();
        return toString();
    }

    /**
     * A factory method, while allows us to use other interfaces to implement
     * the same functionality
     * 
     * @return
     */
    @Override
    public PwFormatter getEmpty() {
        return new PwFormatter();
    }

    /**
     * Ends a grayout
     */
    @Override
    public String grayoutEnd() {
        closeSpan();
        return toString();
    }

    /**
     * Starts a grayout
     */
    @Override
    public String grayoutStart() {
        openSpan("class=" + PwFormatter.CLASS_GRAYOUT);
        return toString();
    }

    /**
     * Starts an indented part
     */
    @Override
    public void indent() {
        openDiv("class=" + PwFormatter.CLASS_INDENT);
    }

    /**
     * Formats a name / value pair
     * 
     * @param name
     * @param object
     */
    @Override
    public void is(String name, Object object) {
        String value;
        if (object == null) {
            value = "<null>";
        } else {
            if (object instanceof Double) {
                value = Formatter.fmt((Double) object);
            } else {
                value = object.toString();
            }
        }
        addLabelParagraph(name, " = " + value);
    }

    /**
     * Shows a progress bar (implemented with span components)
     * 
     * If it is a positive value, it shows dark green on light green.
     * 
     * If it is a negative value, it shows dark red on pink
     * 
     * @param fmt
     * @param value
     * @param maxValue
     */
    @Override
    public String progressBar(double value, double maxValue) {
        return progressBar(value, maxValue, null);
    }

    /**
     * Shows a progress bar (implemented with span components)
     * 
     * If it is a positive value, it shows dark green on light green.
     * 
     * If it is a negative value, it shows dark red on pink
     * 
     * @param fmt
     * @param value
     * @param maxValue
     * @param label
     *            - the label: if null, make it a formatted value
     */
    public String progressBar(double value, double maxValue, String label) {
        String meterWrap = null;
        String meterValue = null;
        String meterText = PwFormatter.CLASS_METER_TEXT;
        try {
            if (maxValue == 0) {
                throw new Error("Max value is zero!!!");
            }
            if (maxValue > 0) {
                meterWrap = PwFormatter.CLASS_METER_WRAP;
                meterValue = PwFormatter.CLASS_METER_VALUE;
            } else {
                meterWrap = PwFormatter.CLASS_METER_WRAP_NEGATIVE;
                meterValue = PwFormatter.CLASS_METER_VALUE_NEGATIVE;
            }
            int percent = (int) (100 * value / maxValue);
            if (percent >= 0 && percent <= 100) {
                openSpan("class=\"" + meterWrap + "\"");
                openSpan("class=\"" + meterValue + "\"", "style=\"width: "
                        + percent + "%;\"");
                openSpan("class=\"" + meterText + "\"");
            }
            //
            // FIXME: this color turning is not good for negative values
            // smaller than zero: turn it pink
            if (percent < 0) {
                percent = 0;
                openSpan("class=\"" + meterWrap + "\"",
                        "style=\"background: #F665AB\""); // pink
                openSpan("class=\"" + meterValue + "\"", "style=\"width: "
                        + percent + "%;\"");
                openSpan("class=\"" + meterText + "\"");
            }
            // larger than maxvalue: turn it red
            if (percent > 100) {
                percent = 100;
                openSpan("class=\"" + meterWrap + "\"");
                openSpan("class=\"" + meterValue + "\"", "style=\"width: "
                        + percent + "%; background: red;\"");
                openSpan("class=\"" + meterText + "\"");
            }
            if (label == null) {
                if (value >= 0) {
                    add(Formatter.fmt(value));
                } else {
                    String text = Formatter.fmt(value);
                    add(text);
                }
            } else {
                add(label);
            }
            closeSpan();
            closeSpan();
            closeSpan();
            return toString();
        } catch (Error ex) {
            TextUi.println(ex);
        }
        return "xxx";
    }

    /**
     * Shows a progress bar with a slash label, normally representing
     * salience/energy. The salience is always on the 0..1 range.
     * 
     * If it is a positive value, it shows dark green on light green. If it is a
     * negative value, it shows dark red on pink
     * 
     * @param fmt
     * @param value
     * @param maxValue
     */
    @Override
    public String progressBarSlash(double salience, double energy) {
        String label = Formatter.fmt(salience) + "/" + Formatter.fmt(energy);
        return progressBar(salience, 1.0, label);
    }

    /**
     * Starts an embedded part
     */
    @Override
    public String startEmbed() {
        openDiv("class=" + PwFormatter.CLASS_EMBED);
        return toString();
    }

    /**
     * Starts an embedded part - with a vertical label
     */
    @Override
    public String startEmbedX(String text) {
        openDiv("class=" + PwFormatter.CLASS_EMBED);
        openTable("class=embedtable");
        openTR();
        openTD("class=embedvertical");
        // vertical text here
        openDiv("class=verticaltext");
        add(text);
        closeDiv();
        closeTD();
        openTD();
        return toString();
    }

}
