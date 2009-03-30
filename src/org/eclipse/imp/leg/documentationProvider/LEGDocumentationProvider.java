package org.eclipse.imp.leg.documentationProvider;

import lpg.runtime.IToken;

import org.eclipse.imp.parser.IParseController;
import org.eclipse.imp.parser.SimpleLPGParseController;
import org.eclipse.imp.services.IDocumentationProvider;

import org.eclipse.imp.leg.parser.Ast.*;

import org.eclipse.imp.leg.parser.LEGParsersym;

public class LEGDocumentationProvider implements IDocumentationProvider {
    public String getDocumentation(Object entity, IParseController ctlr) {
        if (entity == null)
            return null;

        if (entity instanceof ASTNode) {
            // START_HERE
            // Create a case for each kind of node or token for which you
            // want to provide help text and return the text corresponding
            // to that entity (such as in the following examples)

            // Address node types of interest, which may represent multiple tokens
            if (entity instanceof declaration) {
                declaration decl= (declaration) entity;
                IToken[] adjuncts= decl.getPrecedingAdjuncts();

                for(int i=0; i < adjuncts.length; i++) {
                    String adjStr= adjuncts[i].toString();
                    if (adjStr.startsWith("//**")) {
                        return adjStr.substring(4);
                    }
                }
                return decl.getprimitiveType().toString() + " "
                        + decl.getidentifier().toString();
            }
            if (entity instanceof functionDeclaration) {
                functionDeclaration fdecl= (functionDeclaration) entity;
                IToken[] adjuncts= fdecl.getPrecedingAdjuncts();

                for(int i=0; i < adjuncts.length; i++) {
                    String adjStr= adjuncts[i].toString();
                    if (adjStr.startsWith("//**")) {
                        return adjStr.substring(4);
                    }
                }
            }

            // Address node types that can be treated as individual tokens
            int tokenKind= getTokenKindForNode((ASTNode) entity);

            switch (tokenKind) {
            case LEGParsersym.TK_IDENTIFIER:
                return "This is an identifier";
            case LEGParsersym.TK_NUMBER:
                return "This is a number";

            default:
                //return "No documentation available for token kind = " + tokenKind;
                return null;
            }
        }
        return null;
    }

    public int getTokenKindForNode(ASTNode node) {
        // If you want some token for a node
        // other than the right token, compute
        // that here ...
        return node.getRightIToken().getKind();
    }

    public static String getSubstring(IParseController parseController,
            int start, int end) {
        return new String(((SimpleLPGParseController) parseController).getParser().getIPrsStream().getInputChars(),
                start, end - start + 1);
    }
}
