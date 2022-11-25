/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2019 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package com.github.javaparser.printer.lexicalpreservation;

import static com.github.javaparser.utils.TestUtils.assertEqualsStringIgnoringEol;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.utils.LineSeparator;

public class Issue3746Test extends AbstractLexicalPreservingTest {

	@Test
    void test() {
        considerCode(
                "public class MyClass {\n"
                        + " String s0;\n"
                        + " // Comment\n"
                        + " String s1;\n"
                        + "}");

        considerCode("class A {\n"
				+ "  void foo() {\n"
				+ "    int first = 1;\n"
				+ "    int second = 2;\n"
				+ "  }\n"
				+ "}"
				);
    	
    	String expected = 
    			"class A {\n"
    			+ "  void foo() {\n"
    			+ "    foo();\n"
    			+ "    int second = 2;\n"
    			+ "  }\n"
    			+ "}";
    	BlockStmt block = cu.findAll(BlockStmt.class).get(0);
    	ExpressionStmt newStmt = new ExpressionStmt(new MethodCallExpr("foo"));
		block.addStatement(1,newStmt);
		block.getStatement(0).remove();
		assertEquals(expected, LexicalPreservingPrinter.print(cu));
    }
}