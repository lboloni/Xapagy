package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.agents.LoopItem;
import org.xapagy.agents.LoopItem.LoopItemState;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

public class qh_LOOP_ITEM implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        LoopItem li = agent.getLoop().getAllLoopItems().get(identifier);
        if (li == null) {
            fmt.addErrorMessage("The LoopItem with the identifier "
                    + identifier + " could not be found");
            return;
        } else {
        }
        //
        // Identifier block
        //
        String redheader = "LoopItem " + fmt.getEmpty().addIdentifier(li);
        fmt.addH2(redheader, "class=identifier");
        qh_LOOP_ITEM.pwDetailed(fmt, agent, li, query);
    }

    /**
     * Try to summarize in one line the essence of the loop item FIXME: this is
     * for the time being the same as the one in the PpLoopItem
     * 
     * @param li
     * @param agent
     * @return
     */
    public static String pwConcise(PwFormatter fmt, LoopItem li, Agent agent) {
        // prefix: executed / not executed
        switch (li.getState()) {
        case EXECUTED:
            fmt.addEnum(Formatter.fmt(li.getExecutionTime()));
            break;
        case NOT_EXECUTED:
            break;
        }
        // /
        switch (li.getType()) {
        case EXTERNAL: {
            fmt.addEnum("External: ");
            fmt.add("-- dont know how to handle external");
            break;
        }
        case INTERNAL: {
            fmt.addEnum("Internal: ");
            fmt.add(PrettyPrint.ppConcise(li.getChoice(), agent));
            break;
        }
        case FORCED: {
            fmt.addEnum("Forced: ");
            fmt.add(PrettyPrint.ppConcise(li.getText(), agent));
            break;
        }
        case READING: {
            fmt.addEnum("Reading: ");
            fmt.add(li.getText());
            break;
        }
        }
        return fmt.toString();
    }

    /**
     * Prints a LoopItem in such a way that it can be embedded in other
     * components
     * 
     * @param fmt
     * @param agent
     * @param li
     * @param query
     * @return
     */
    public static String pwDetailed(PwFormatter fmt, Agent agent, LoopItem li,
            RESTQuery query) {
        fmt.is("Type", fmt.getEmpty().addEnum(li.getType().toString()));
        fmt.is("State", fmt.getEmpty().addEnum(li.getState().toString()));
        if (li.getState().equals(LoopItemState.EXECUTED)) {
            fmt.is("Execution time", li.getExecutionTime());
            fmt.addBold("Execution results:");
            fmt.indent();
            for (VerbInstance vi : li.getExecutionResult()) {
                fmt.openP();
                PwQueryLinks.linkToVi(fmt, agent, query, vi);
                fmt.closeP();
            }
            fmt.deindent();
        }

        switch (li.getType()) {
        case EXTERNAL: {
            fmt.is("scheduled time", li.getScheduledExecutionTime());
            fmt.is("text", li.getText());
            break;
        }
        case INTERNAL: {
            fmt.addBold("choice");
            fmt.startEmbed();
            qh_CHOICE.pwDetailed(fmt, agent, li.getChoice(), query);
            fmt.endEmbed();
            break;
        }
        case READING: {
            String header = "Reading: ";
            if (li.getFileName() != null) {
                header =
                        header + " (" + li.getFileName() + ":"
                                + li.getFileLineNo() + ")";
            } else {
                header = header + "(directly added)";
            }
            fmt.add(header);
            fmt.add(li.getText());
            break;
        }
        }
        return fmt.toString();
    }

}
