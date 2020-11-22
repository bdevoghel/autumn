package norswap.lang.java;

import norswap.autumn.DSL;
import norswap.autumn.StackAction;
import norswap.lang.java.ast.*;
import norswap.lang.java.ast.TypeDeclaration.Kind;
import norswap.utils.Pair;

import static java.util.Collections.emptyList;
import static norswap.lang.java.LexUtils.*;
import static norswap.lang.java.ast.BinaryOperator.*;
import static norswap.lang.java.ast.UnaryOperator.*;

public final class Grammar extends DSL
{
    /// LEXICAL ====================================================================================

    // Whitespace ----------------------------------------------------------------------------------

    public rule space_char          = cpred(Character::isWhitespace);
    public rule not_line            = seq(str("\n").not(), any);
    public rule line_comment        = seq("//", not_line.at_least(0), str("\n").opt());

    public rule not_comment_term    = seq(str("*/").not(), any);
    public rule multi_comment       = seq("/*", not_comment_term.at_least(0), "*/");

    public rule whitespace          = choice(space_char, line_comment, multi_comment);

    { ws = whitespace.at_least(0); }

    // Keywords and Operators ----------------------------------------------------------------------

    public rule _boolean        = word("boolean")      .token();
    public rule _byte           = word("byte")         .token();
    public rule _char           = word("char")         .token();
    public rule _double         = word("double")       .token();
    public rule _float          = word("float")        .token();
    public rule _int            = word("int")          .token();
    public rule _long           = word("long")         .token();
    public rule _short          = word("short")        .token();
    public rule _void           = word("void")         .token();
    public rule _abstract       = word("abstract")     .token();
    public rule _default        = word("default")      .token();
    public rule _final          = word("final")        .token();
    public rule _native         = word("native")       .token();
    public rule _private        = word("private")      .token();
    public rule _protected      = word("protected")    .token();
    public rule _public         = word("public")       .token();
    public rule _static         = word("static")       .token();
    public rule _strictfp       = word("strictfp")     .token();
    public rule _synchronized   = word("synchronized") .token();
    public rule _transient      = word("transient")    .token();
    public rule _volatile       = word("volatile")     .token();
    public rule _assert         = word("assert")       .token();
    public rule _break          = word("break")        .token();
    public rule _case           = word("case")         .token();
    public rule _catch          = word("catch")        .token();
    public rule _class          = word("class")        .token();
    public rule _const          = word("const")        .token();
    public rule _continue       = word("continue")     .token();
    public rule _do             = word("do")           .token();
    public rule _else           = word("else")         .token();
    public rule _enum           = word("enum")         .token();
    public rule _extends        = word("extends")      .token();
    public rule _finally        = word("finally")      .token();
    public rule _for            = word("for")          .token();
    public rule _goto           = word("goto")         .token();
    public rule _if             = word("if")           .token();
    public rule _implements     = word("implements")   .token();
    public rule _import         = word("import")       .token();
    public rule _interface      = word("interface")    .token();
    public rule _instanceof     = word("instanceof")   .token();
    public rule _new            = word("new")          .token();
    public rule _package        = word("package")      .token();
    public rule _return         = word("return")       .token();
    public rule _super          = word("super")        .token();
    public rule _switch         = word("switch")       .token();
    public rule _this           = word("this")         .token();
    public rule _throws         = word("throws")       .token();
    public rule _throw          = word("throw")        .token();
    public rule _try            = word("try")          .token();
    public rule _while          = word("while")        .token();

    // Names are taken from the javac8 lexer.
    // https://github.com/dmlloyd/openjdk/blob/jdk8u/jdk8u/langtools/src/share/classes/com/sun/tools/javac/parser/Tokens.java
    // Ordering matters when there are shared prefixes!

    public rule BANG            = word("!")            .token();
    public rule BANGEQ          = word("!=")           .token();
    public rule PERCENT         = word("%")            .token();
    public rule PERCENTEQ       = word("%=")           .token();
    public rule AMP             = word("&")            .token();
    public rule AMPAMP          = word("&&")           .token();
    public rule AMPEQ           = word("&=")           .token();
    public rule LPAREN          = word("(")            .token();
    public rule RPAREN          = word(")")            .token();
    public rule STAR            = word("*")            .token();
    public rule STAREQ          = word("*=")           .token();
    public rule PLUS            = word("+")            .token();
    public rule PLUSPLUS        = word("++")           .token();
    public rule PLUSEQ          = word("+=")           .token();
    public rule COMMA           = word(",")            .token();
    public rule SUB             = word("-")            .token();
    public rule SUBSUB          = word("--")           .token();
    public rule SUBEQ           = word("-=")           .token();
    public rule EQ              = word("=")            .token();
    public rule EQEQ            = word("==")           .token();
    public rule QUES            = word("?")            .token();
    public rule CARET           = word("^")            .token();
    public rule CARETEQ         = word("^=")           .token();
    public rule LBRACE          = word("{")            .token();
    public rule RBRACE          = word("}")            .token();
    public rule BAR             = word("|")            .token();
    public rule BARBAR          = word("||")           .token();
    public rule BAREQ           = word("|=")           .token();
    public rule TILDE           = word("~")            .token();
    public rule MONKEYS_AT      = word("@")            .token();
    public rule DIV             = word("/")            .token();
    public rule DIVEQ           = word("/=")           .token();
    public rule GTEQ            = word(">=")           .token();
    public rule LTEQ            = word("<=")           .token();
    public rule LTLTEQ          = word("<<=")          .token();
    public rule LTLT            = word("<<")           .token();
    public rule GTGTEQ          = word(">>=")          .token();
    public rule GTGTGTEQ        = word(">>>=")         .token();
    public rule GT              = word(">")            .token();
    public rule LT              = word("<")            .token();
    public rule LBRACKET        = word("[")            .token();
    public rule RBRACKET        = word("]")            .token();
    public rule ARROW           = word("->")           .token();
    public rule COL             = word(":")            .token();
    public rule COLCOL          = word("::")           .token();
    public rule SEMI            = word(";")            .token();
    public rule DOT             = word(".")            .token();
    public rule ELLIPSIS        = word("...")          .token();

    // These two are not tokens, because they would cause issue with nested generic types.
    // e.g. in List<List<String>>, you want ">>" to lex as [_GT, _GT]

    public rule GTGT            = word(">>");
    public rule GTGTGT          = word(">>>");

    public rule _false          = word("false")     .as_val(false)      .token();
    public rule _true           = word("true")      .as_val(true)       .token();
    public rule _null           = word("null")      .as_val(Null.NULL)  .token();

    // Identifiers ---------------------------------------------------------------------------------

    public rule id_start    = cpred(Character::isJavaIdentifierStart);
    public rule id_part     = cpred(c -> c != 0 && Character.isJavaIdentifierPart(c));

    public rule iden = seq(id_start, id_part.at_least(0))
        .push(with_string((p,xs,str) -> Identifier.mk(str)))
        .word()
        .token();

    // Numerals - Common Parts ---------------------------------------------------------------------

    public rule underscore  = str("_");
    public rule dlit        = str(".");
    public rule hex_prefix  = choice("0x", "0X");
    public rule underscores = underscore.at_least(0);
    public rule digits1     = digit.sep(1, underscores);
    public rule digits0     = digit.sep(0, underscores);
    public rule hex_digits  = hex_digit.sep(1, underscores);
    public rule hex_num     = seq(hex_prefix, hex_digits);

    // Numerals - Floating Point -------------------------------------------------------------------

    public rule hex_significand = choice(
        seq(hex_prefix, hex_digits.opt(), dlit, hex_digits),
        seq(hex_num, dlit.opt()));

    public rule exp_sign_opt        = set("+-").opt();
    public rule exponent            = seq(set("eE"), exp_sign_opt, digits1);
    public rule binary_exponent     = seq(set("pP"), exp_sign_opt, digits1);
    public rule float_suffix        = set("fFdD");
    public rule float_suffix_opt    = float_suffix.opt();
    public rule hex_float_lit       = seq(hex_significand, binary_exponent, float_suffix_opt);

    public rule decimal_float_lit = choice(
        seq(digits1, dlit, digits0, exponent.opt(), float_suffix_opt),
        seq(dlit, digits1, exponent.opt(), float_suffix_opt),
        seq(digits1, exponent, float_suffix_opt),
        seq(digits1, exponent.opt(), float_suffix));

    public rule float_literal = choice(hex_float_lit, decimal_float_lit)
        .push(with_string((p,xs,str) -> parse_floating(str).unwrap()))
        .token();

    // Numerals - Integral -------------------------------------------------------------------------

    public rule bit             = set("01");
    public rule binary_prefix   = choice("0b", "0B");
    public rule binary_num      = seq(binary_prefix, bit.at_least(1).sep(1, underscores));
    public rule octal_num       = seq("0", seq(underscores, octal_digit).at_least(1));
    public rule decimal_num     = choice("0", digits1);
    public rule integer_num     = choice(hex_num, binary_num, octal_num, decimal_num);

    public rule integer_literal = seq(integer_num, set("lL").opt())
        .push(with_string((p,xs,str) -> parse_integer(str).unwrap()))
        .token();

    // Characters and Strings ----------------------------------------------------------------------

    public rule octal_code_3    = seq(range('0', '3'), octal_digit, octal_digit);
    public rule octal_code_2    = seq(octal_digit, octal_digit.opt());
    public rule octal_code      = choice(octal_code_3, octal_code_2);
    public rule unicode_code    = seq(str("u").at_least(1), hex_digit.repeat(4));
    public rule escape_suffix   = choice(set("btnfr\"'\\"), octal_code, unicode_code);
    public rule escape          = seq("\\", escape_suffix);
    public rule naked_char      = choice(escape, seq(set("'\\\n\r").not(), any));
    public rule nake_str_char   = choice(escape, seq(set("\"\\\n\r").not(), any));

    public rule char_literal = seq("'", naked_char, "'")
        .push(with_string((p,xs,str) -> parse_char(str).unwrap()))
        .token();

    public rule string_literal = seq("\"", nake_str_char.at_least(0), "\"")
        .push(with_string((p,xs,str) -> parse_string(str).unwrap()))
        .token();

    // Literal ----------------------------------------------------------------

    public rule literal = token_choice(
            integer_literal, string_literal, _null, float_literal, _true, _false, char_literal)
        .word()
        .push(xs -> Literal.mk(xs[0]));

    //// LAZY FORWARD REFS =========================================================================

    public rule _stmt =
        lazy(() -> this.stmt);

    public rule _expr =
        lazy(() -> this.expr);

    public rule _block =
        lazy(() -> this.block);

    /// ANNOTATIONS ================================================================================

    public rule annotation_element = choice(
        lazy(() -> this.ternary_expr),
        lazy(() -> this.annotation_element_list),
        lazy(() -> this.annotation));

    public rule annotation_inner_list =
        lazy(() -> this.annotation_element).sep_trailing(0, COMMA);

    public rule annotation_element_list =
        seq(LBRACE, annotation_inner_list, RBRACE)
        .push(xs -> AnnotationElementList.mk(list(xs)));

    public rule annotation_element_pair =
        seq(iden, EQ, annotation_element)
        .push(xs -> new Pair<Identifier, AnnotationElement>($(xs,0), $(xs,1)));

    public rule normal_annotation_suffix =
        seq(LPAREN, annotation_element_pair.sep(1, COMMA), RPAREN)
        .push(with_parse((p,xs) -> NormalAnnotation.mk($(p.stack.pop()), list(xs))));

    public rule single_element_annotation_suffix =
        seq(LPAREN, annotation_element, RPAREN)
        .collect().lookback(1).push(xs -> SingleElementAnnotation.mk($(xs,0), $(xs,1)));

    public rule marker_annotation_suffix =
        seq(LPAREN, RPAREN).opt()
         .collect().lookback(1).push(xs -> MarkerAnnotation.mk($(xs,0)));

    public rule annotation_suffix = choice(
        normal_annotation_suffix,
        single_element_annotation_suffix,
        marker_annotation_suffix);

    public rule qualified_iden =
        iden.sep(1, DOT)
        .collect().as_list(Identifier.class);

    public rule annotation =
        seq(MONKEYS_AT, qualified_iden, annotation_suffix);

    public rule annotations =
        annotation.at_least(0)
        .collect().as_list(TAnnotation.class);

    /// TYPES ======================================================================================

    public rule basic_type =
        token_choice(_byte, _short, _int, _long, _char, _float, _double, _boolean, _void)
        .push(with_string((p,xs,str) -> BasicType.valueOf("_" + trim_trailing_whitespace(str))));

    public rule primitive_type =
        seq(annotations, basic_type)
        .push(xs -> PrimitiveType.mk($(xs,0), $(xs,1)));

    public rule extends_bound =
        seq(_extends, lazy(() -> this.type))
        .push(xs -> ExtendsBound.mk($(xs,0)));

    public rule super_bound =
        seq(_super, lazy(() -> this.type))
        .push(xs -> SuperBound.mk($(xs,0)));

    public rule type_bound =
        choice(extends_bound, super_bound).maybe();

    public rule wildcard =
        seq(annotations, QUES, type_bound)
        .push(xs -> Wildcard.mk($(xs,0), $(xs,1)));

    public rule opt_type_args =
        seq(LT, choice(lazy(() -> this.type), wildcard).sep(0, COMMA), GT).opt()
        .collect().as_list(TType.class);

    public rule class_type_part =
        seq(annotations, iden, opt_type_args)
        .push(xs -> ClassTypePart.mk($(xs, 0), $(xs, 1), $(xs, 2)));

    public rule class_type =
        class_type_part.sep(1, DOT)
        .push(xs -> ClassType.mk(list(xs)));

    public rule stem_type =
        choice(primitive_type, class_type);

    public rule dim =
        seq(annotations, seq(LBRACKET, RBRACKET))
        .push(xs -> Dimension.mk($(xs,0)));

    public rule dims =
        dim.at_least(0)
        .collect().as_list(Dimension.class);

    public rule dims1 =
        dim.at_least(1)
        .collect().as_list(Dimension.class);

    public rule type_dim_suffix =
        dims1
        .collect().lookback(1).push(xs -> ArrayType.mk($(xs,0), $(xs,1)));

    public rule type =
        seq(stem_type, type_dim_suffix.opt());

    public rule type_union_syntax =
        lazy(() -> this.type).sep(1, AMP);

    public rule type_union =
        type_union_syntax
        .collect().as_list(TType.class);

    public rule type_bounds =
        seq(_extends, type_union_syntax).opt()
        .collect().as_list(TType.class);

    public rule type_param =
        seq(annotations, iden, type_bounds)
        .push(xs -> TypeParameter.mk($(xs,0), $(xs,1), $(xs,2)));

    public rule type_params =
        seq(LT, type_param.sep(0, COMMA), GT).opt()
        .collect().as_list(TypeParameter.class);

    /// EXPRESSIONS ================================================================================

    // Initializers -----------------------------------------------------------

    public rule var_init =
        choice(_expr, lazy(() -> this.array_init));

    public rule array_init =
        seq(LBRACE, var_init.sep_trailing(0, COMMA), RBRACE)
        .push(xs -> ArrayInitializer.mk(list(xs)));

    // Array Constructor ------------------------------------------------------

    public rule dim_expr =
        seq(annotations, LBRACKET, _expr, RBRACKET)
        .push(xs -> DimExpression.mk($(xs,0), $(xs,1)));

    public rule dim_exprs =
        dim_expr.at_least(1)
        .collect().as_list(DimExpression.class);

    public rule dim_expr_array_creator =
        seq(stem_type, dim_exprs, dims)
        .push(xs -> ArrayConstructorCall.mk($(xs,0), $(xs,1), $(xs,2), null));

    public rule init_array_creator =
        seq(stem_type, dims1, array_init)
        .push(xs -> ArrayConstructorCall.mk($(xs,0), emptyList(), $(xs,1), $(xs,2)));

    public rule array_ctor_call =
        seq(_new, choice(dim_expr_array_creator, init_array_creator));

    // Lambda Expression ------------------------------------------------------

    public rule lambda = lazy(() ->
        seq(this.lambda_params, ARROW, choice(this.block, this.expr)))
        .push(xs -> Lambda.mk($(xs,0), $(xs,1)));

    // Expression - Primary ---------------------------------------------------

    public rule args =
        seq(LPAREN, _expr.sep(0, COMMA), RPAREN)
        .collect().as_list(Expression.class);

    public rule par_expr =
        seq(LPAREN, _expr, RPAREN)
        .push(xs -> ParenExpression.mk($(xs,0)));

    public rule ctor_call =
        seq(_new, opt_type_args, stem_type, args, lazy(() -> this.type_body).maybe())
        .push(xs -> ConstructorCall.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)));

    public rule new_ref_suffix =
        _new
        .collect().lookback(2).push(xs -> NewReference.mk($(xs,0), $(xs,1)));

    public rule method_ref_suffix =
        iden
        .collect().lookback(2).push(xs -> TypeMethodReference.mk($(xs,0), $(xs,1), $(xs,2)));

    public rule ref_suffix =
        seq(COLCOL, opt_type_args, choice(method_ref_suffix, new_ref_suffix));

    public rule class_expr_suffix =
        seq(DOT, _class)
        .collect().lookback(1).push(xs -> ClassExpression.mk($(xs,0)));

    public rule type_suffix_expr =
        seq(type, choice(ref_suffix, class_expr_suffix));

    public rule iden_or_method_expr =
        seq(iden, args.maybe())
        .push(xs -> $(xs,1) == null ? $(xs,0) : MethodCall.mk(null, list(), $(xs,0), $(xs,1)));

    public rule this_expr =
        seq(_this, args.maybe())
        .push(xs -> $(xs,0) == null ? This.mk() : ThisCall.mk($(xs,0)));

    public rule super_expr =
        seq(_super, args.maybe())
        .push(xs -> $(xs,0) == null ? Super.mk() : SuperCall.mk($(xs,0)));

    public rule primary_expr = choice(
        lambda, par_expr, array_ctor_call, ctor_call, type_suffix_expr, iden_or_method_expr,
        this_expr, super_expr, literal);

    // Expression - Postfix & Prefix ------------------------------------------

    public rule prefix_op = choice(
        PLUSPLUS    .as_val(PREFIX_INCREMENT),
        SUBSUB      .as_val(PREFIX_DECREMENT),
        PLUS        .as_val(UNARY_PLUS),
        SUB         .as_val(UNARY_MINUS),
        TILDE       .as_val(BITWISE_COMPLEMENT),
        BANG        .as_val(LOGICAL_COMPLEMENT));

    public rule postfix_expr = left_expression()
        .left(primary_expr)
        .suffix(seq(DOT, opt_type_args, iden, args),
            xs -> MethodCall.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)))
        .suffix(seq(DOT, iden),
            xs -> DotIden.mk($(xs,0), $(xs,1)))
        .suffix(seq(DOT, _this),
            xs -> UnaryExpression.mk(DOT_THIS, $(xs,0)))
        .suffix(seq(DOT, _super),
            xs -> UnaryExpression.mk(DOT_SUPER, $(xs,0)))
        .suffix(seq(DOT, ctor_call),
            xs -> DotNew.mk($(xs,0), $(xs,1)))
        .suffix(seq(LBRACKET, _expr, RBRACKET),
            xs -> ArrayAccess.mk($(xs,0), $(xs,1)))
        .suffix(PLUSPLUS,
            xs -> UnaryExpression.mk(POSTFIX_INCREMENT, $(xs,0)))
        .suffix(SUBSUB,
            xs -> UnaryExpression.mk(POSTFIX_DECREMENT, $(xs,0)))
        .suffix(seq(COLCOL, opt_type_args, iden),
            xs -> BoundMethodReference.mk($(xs,0), $(xs,1), $(xs,2)))
        .get();

    public rule prefix_expr = recursive(self -> choice(
        seq(prefix_op, self)
            .push(xs -> UnaryExpression.mk($(xs,0), $(xs,1))),
        seq(LPAREN, type_union, RPAREN, self)
            .push(xs -> Cast.mk($(xs,0), $(xs,1))),
        postfix_expr));

    // Expression - Binary ----------------------------------------------------

    StackAction.Push binary_push =
        xs -> BinaryExpression.mk($(xs,1), $(xs,0), $(xs,2));

    public rule mult_op = choice(
        STAR        .as_val(MULTIPLY),
        DIV         .as_val(DIVIDE),
        PERCENT     .as_val(REMAINDER));

    public rule add_op = choice(
        PLUS        .as_val(ADD),
        SUB         .as_val(SUBTRACT));

    public rule shift_op = choice(
        LTLT        .as_val(LEFT_SHIFT),
        GTGTGT      .as_val(UNSIGNED_RIGHT_SHIFT),
        GTGT        .as_val(RIGHT_SHIFT));

    public rule order_op = choice(
        LTEQ        .as_val(LESS_THAN_EQUAL),
        LT          .as_val(LESS_THAN),
        GTEQ        .as_val(GREATER_THAN_EQUAL),
        GT          .as_val(GREATER_THAN));

    public rule eq_op = choice(
        EQEQ        .as_val(EQUAL_TO),
        BANGEQ      .as_val(NOT_EQUAL_TO));

    public rule assignment_op = choice(
        EQ          .as_val(ASSIGNMENT),
        PLUSEQ      .as_val(ADD_ASSIGNMENT),
        SUBEQ       .as_val(SUBTRACT_ASSIGNMENT),
        STAREQ      .as_val(MULTIPLY_ASSIGNMENT),
        DIVEQ       .as_val(DIVIDE_ASSIGNMENT),
        PERCENTEQ   .as_val(REMAINDER_ASSIGNMENT),
        LTLTEQ      .as_val(LEFT_SHIFT_ASSIGNMENT),
        GTGTEQ      .as_val(RIGHT_SHIFT_ASSIGNMENT),
        GTGTGTEQ    .as_val(UNSIGNED_RIGHT_SHIFT_ASSIGNMENT),
        AMPEQ       .as_val(AND_ASSIGNMENT),
        CARETEQ     .as_val(XOR_ASSIGNMENT),
        BAREQ       .as_val(OR_ASSIGNMENT));

    public rule mult_expr = left_expression()
        .operand(prefix_expr)
        .infix(mult_op, binary_push).get();

    public rule add_expr = left_expression()
        .operand(mult_expr)
        .infix(add_op, binary_push).get();

    public rule shift_expr = left_expression()
        .operand(add_expr)
        .infix(shift_op, binary_push).get();

    public rule order_expr = left_expression()
        .operand(shift_expr)
        .suffix(seq(_instanceof, type),
            xs -> InstanceOf.mk($(xs,0), $(xs,1)))
        .infix(order_op, binary_push)
        .get();

    public rule eq_expr = left_expression()
        .operand(order_expr)
        .infix(eq_op, binary_push).get();

    public rule binary_and_expr = left_expression()
        .operand(eq_expr)
        .infix(AMP.as_val(AND), binary_push).get();

    public rule xor_expr = left_expression()
        .operand(binary_and_expr)
        .infix(CARET.as_val(XOR), binary_push).get();

    public rule binary_or_expr = left_expression()
        .operand(xor_expr)
        .infix(BAR.as_val(OR), binary_push).get();

    public rule conditional_and_expr = left_expression()
        .operand(binary_or_expr)
        .infix(AMPAMP.as_val(CONDITIONAL_AND), binary_push).get();

    public rule conditional_or_expr = left_expression()
        .operand(conditional_and_expr)
        .infix(BARBAR.as_val(CONDITIONAL_OR), binary_push).get();

    public rule ternary_expr = right_expression()
        .operand(conditional_or_expr)
        .infix(seq(QUES, _expr, COL),
            xs -> TernaryExpression.mk($(xs,0), $(xs,1), $(xs,2)))
        .get();

    public rule expr = right_expression()
        .operand(ternary_expr)
        .infix(assignment_op, binary_push).get();

    /// MODIFIERS ==================================================================================

    public rule keyword_modifier =
        token_choice(
            _public, _protected, _private, _abstract, _static, _final, _synchronized,
            _native, _strictfp, _default, _transient, _volatile)
            .push(with_string((p,xs,str) -> Keyword.valueOf("_" + trim_trailing_whitespace(str))));

    public rule modifier =
        choice(annotation, keyword_modifier);

    public rule modifiers =
        modifier.at_least(0)
        .collect().as_list(Modifier.class);

    /// PARAMETERS =================================================================================

    public rule this_parameter_qualifier =
        seq(iden, DOT).at_least(0)
        .collect().as_list(String.class);

    public rule this_param_suffix =
        seq(this_parameter_qualifier, _this)
        .collect().lookback(2)
        .push(xs -> ThisParameter.mk($(xs,0), $(xs,1), $(xs,2)));

    public rule iden_param_suffix =
        seq(iden, dims)
        .collect().lookback(2)
        .push(xs -> IdenParameter.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)));

    public rule variadic_param_suffix =
        seq(annotations, ELLIPSIS, iden)
        .collect().lookback(2)
        .push(xs -> VariadicParameter.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)));

    public rule formal_param_suffix =
        choice(iden_param_suffix, this_param_suffix, variadic_param_suffix);

    public rule formal_param =
        seq(modifiers, type, formal_param_suffix);

    public rule formal_params =
        seq(LPAREN, formal_param.sep(0, COMMA), RPAREN)
        .push(xs -> FormalParameters.mk(list()));

    public rule untyped_params =
        seq(LPAREN, iden.sep(1, COMMA), RPAREN)
        .push(xs -> UntypedParameters.mk(list()));

    public rule single_param =
        iden
        .push(xs -> UntypedParameters.mk(list(xs)));

    public rule lambda_params =
        choice(formal_params, untyped_params, single_param);

    /// NON-TYPE DECLARATIONS ======================================================================

    public rule var_declarator_id =
        seq(iden, dims)
        .push(xs -> VarDeclaratorID.mk($(xs,0), $(xs,1)));

    public rule var_declarator =
        seq(var_declarator_id, seq(EQ, var_init).maybe())
        .push(xs -> VarDeclarator.mk($(xs,0), $(xs,1)));

    public rule var_declarators =
        var_declarator.sep(1, COMMA)
        .collect().as_list(VarDeclarator.class);

    public rule var_decl_suffix_no_semi =
        seq(type, var_declarators)
        .collect().lookback(1)
        .push(xs -> VarDeclaration.mk($(xs,0), $(xs,1), $(xs,2)));

    public rule var_decl_suffix =
        seq(var_decl_suffix_no_semi, SEMI);

    public rule var_decl =
        seq(modifiers, var_decl_suffix);

    public rule throws_clause =
        seq(_throws, type.sep(1, COMMA)).opt()
        .collect().as_list(TType.class);

    public rule block_or_semi =
        choice(_block, SEMI.as_val(null));

    public rule method_decl_suffix =
        seq(type_params, type, iden, formal_params, dims, throws_clause, block_or_semi)
        .collect().lookback(1)
        .push(xs -> MethodDeclaration.mk(
            $(xs,0), $(xs,1), $(xs,2), $(xs,3), $(xs,4), $(xs,5), $(xs,6), $(xs,7)));

    public rule constructor_decl_suffix =
        seq(type_params, iden, formal_params, throws_clause, _block)
        .collect().lookback(1)
        .push(xs -> ConstructorDeclaration.mk(
            $(xs,0), $(xs,1), $(xs,2), $(xs,3), $(xs,4), $(xs,5)));

    public rule init_block =
        seq(_static.as_bool(), _block)
        .push(xs -> InitBlock.mk($(xs,0), $(xs,1)));

    /// TYPE DECLARATIONS ==========================================================================

    // Common -----------------------------------------------------------------

    public rule extends_clause =
        seq(_extends, type.sep(0, COMMA)).opt()
        .collect().as_list(TType.class);

    public rule implements_clause =
        seq(_implements, type.sep(0, COMMA)).opt()
        .collect().as_list(TType.class);

    public rule type_sig =
        seq(iden, type_params, extends_clause, implements_clause);

    public rule class_modifierized_decl = seq(
        modifiers,
        choice(
            var_decl_suffix,
            method_decl_suffix,
            constructor_decl_suffix,
            lazy(() -> this.type_decl_suffix)));

    public rule class_body_decl =
        choice(class_modifierized_decl, init_block, SEMI);

    public rule class_body_decls =
        class_body_decl.at_least(0)
        .collect().as_list(Declaration.class);

    public rule type_body =
        seq(LBRACE, class_body_decls, RBRACE);

    // Enum -------------------------------------------------------------------

    public rule enum_constant =
        seq(annotations, iden, args.maybe(), type_body.maybe())
        .push(xs -> EnumConstant.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)));

    public rule enum_class_decls =
        seq(SEMI, class_body_decl.at_least(0)).opt();

    public rule enum_constants =
        enum_constant.sep_trailing(1, COMMA).opt();

    public rule enum_body =
        seq(LBRACE, enum_constants, enum_class_decls, RBRACE)
        .collect().as_list(Declaration.class);

    public rule enum_decl_suffix =
        seq(_enum, type_sig, enum_body)
        .collect().lookback(1)
        .push(xs -> TypeDeclaration.mk(Kind.ENUM,
            $(xs,0), $(xs,1), $(xs,2), $(xs,3), $(xs,4), $(xs,5)));

    // Annotations ------------------------------------------------------------

    public rule annot_default_clause =
        seq(_default, annotation_element)
        .push(xs -> $(xs,0));

    public rule annot_elem_decl =
        seq(modifiers, type, iden, LPAREN, RPAREN, dims, annot_default_clause.maybe(), SEMI)
        .push(xs -> AnnotationElementDeclaration.mk(
            $(xs,0), $(xs,1), $(xs,2), $(xs,3), $(xs,4)));

    public rule annot_body_decls =
        choice(annot_elem_decl, class_body_decl).at_least(0)
        .collect().as_list(Declaration.class);

    public rule annotation_decl_suffix =
        seq(MONKEYS_AT, _interface, type_sig, LBRACE, annot_body_decls, RBRACE)
        .collect().lookback(1)
        .push(xs -> TypeDeclaration.mk(Kind.ANNOTATION,
            $(xs,0), $(xs,1), $(xs,2), $(xs,3), $(xs,4), $(xs,5)));

    //// ------------------------------------------------------------------------

    public rule class_decl_suffix =
        seq(_class, type_sig, type_body)
        .collect().lookback(1)
        .push(xs -> TypeDeclaration.mk(Kind.CLASS,
            $(xs,0), $(xs,1), $(xs,2), $(xs,3), $(xs,4), $(xs,5)));

    public rule interface_declaration_suffix =
        seq(_interface, type_sig, type_body)
        .collect().lookback(1)
        .push(xs -> TypeDeclaration.mk(Kind.INTERFACE,
            $(xs,0), $(xs,1), $(xs,2), $(xs,3), $(xs,4), $(xs,5)));

    public rule type_decl_suffix = choice(
        class_decl_suffix,
        interface_declaration_suffix,
        enum_decl_suffix,
        annotation_decl_suffix);

    public rule type_decl =
        seq(modifiers, type_decl_suffix);

    public rule type_decls =
        choice(type_decl, SEMI).at_least(0)
        .collect().as_list(Declaration.class);

    /// STATEMENTS =================================================================================

    public rule if_stmt =
        seq(_if, par_expr, _stmt, seq(_else, _stmt).maybe())
        .push(xs -> IfStatement.mk($(xs,0), $(xs,1), $(xs,2)));

    public rule expr_stmt_list =
        expr.sep(0, COMMA)
        .collect().as_list(Statement.class);

    public rule for_init_decl =
        seq(modifiers, var_decl_suffix_no_semi)
        .collect().as_list(Statement.class);

    public rule for_init =
        choice(for_init_decl, expr_stmt_list);

    public rule basic_for_paren_part =
        seq(for_init, SEMI, expr.maybe(), SEMI, expr_stmt_list.opt());

    public rule basic_for_stmt =
        seq(_for, LPAREN, basic_for_paren_part, RPAREN, _stmt)
        .push(xs -> BasicForStatement.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)));

    public rule for_val_decl =
        seq(modifiers, type, var_declarator_id, COL, expr);

    public rule enhanced_for_stmt =
        seq(_for, LPAREN, for_val_decl, RPAREN, _stmt)
        .push(xs -> EnhancedForStatement.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3), $(xs,4)));

    public rule while_stmt =
        seq(_while, par_expr, _stmt)
        .push(xs -> WhileStatement.mk($(xs,0), $(xs,1)));

    public rule do_while_stmt =
        seq(_do, _stmt, _while, par_expr, SEMI)
        .push(xs -> DoWhileStatement.mk($(xs,0), $(xs,1)));

    public rule catch_parameter_types =
        type.sep(0, BAR)
        .collect().as_list(TType.class);

    public rule catch_parameter =
        seq(modifiers, catch_parameter_types, var_declarator_id);

    public rule catch_clause =
        seq(_catch, LPAREN, catch_parameter, RPAREN, _block)
        .push(xs -> CatchClause.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)));

    public rule catch_clauses =
        catch_clause.at_least(0)
        .collect().as_list(CatchClause.class);

    public rule finally_clause =
        seq(_finally, _block);

    public rule resource =
        seq(modifiers, type, var_declarator_id, EQ, expr)
        .push(xs -> TryResource.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)));

    public rule resources =
        seq(LPAREN, resource.sep_trailing(1, SEMI), RPAREN).opt()
        .collect().as_list(TryResource.class);

    public rule try_stmt =
        seq(_try, resources, _block, catch_clauses, finally_clause.maybe())
        .push(xs -> TryStatement.mk($(xs,0), $(xs,1), $(xs,2), $(xs,3)));

    public rule default_label =
        seq(_default, COL)
        .push(xs -> DefaultLabel.mk());

    public rule case_label =
        seq(_case, expr, COL)
        .push(xs -> CaseLabel.mk($(xs,0)));

    public rule switch_label =
        choice(case_label, default_label);

    public rule switch_clause =
        seq(switch_label, lazy(() -> this.statements))
        .push(xs -> SwitchClause.mk($(xs,0), $(xs,1)));

    public rule switch_stmt =
        seq(_switch, par_expr, LBRACE, switch_clause.at_least(0), RBRACE)
        .push(xs -> SwitchStatement.mk($(xs,0), list(1, xs)));

    public rule synchronized_stmt =
        seq(_synchronized, par_expr, _block)
        .push(xs -> SynchronizedStatement.mk($(xs,0), $(xs,1)));

    public rule return_stmt =
        seq(_return, expr.maybe(), SEMI)
        .push(xs -> ReturnStatement.mk($(xs,0)));

    public rule throw_stmt =
        seq(_throw, expr, SEMI)
        .push(xs -> ThrowStatement.mk($(xs,0)));

    public rule break_stmt =
        seq(_break, iden.maybe(), SEMI)
        .push(xs -> BreakStatement.mk($(xs,0)));

    public rule continue_stmt =
        seq(_continue, iden.maybe(), SEMI)
        .push(xs -> ContinueStatement.mk($(xs,0)));

    public rule assert_stmt =
        seq(_assert, expr, seq(COL, expr).maybe(), SEMI)
        .push(xs -> AssertStatement.mk($(xs,0), $(xs,1)));

    public rule semi_stmt =
        SEMI
        .push(xs -> SemiStatement.mk());

    public rule expr_stmt =
        seq(expr, SEMI);

    public rule labelled_stmt =
        seq(iden, COL, _stmt)
        .push(xs -> LabelledStatement.mk($(xs,0), $(xs,1)));

    public rule stmt = choice(
        _block,
        if_stmt,
        basic_for_stmt,
        enhanced_for_stmt,
        while_stmt,
        do_while_stmt,
        try_stmt,
        switch_stmt,
        synchronized_stmt,
        return_stmt,
        throw_stmt,
        break_stmt,
        continue_stmt,
        assert_stmt,
        semi_stmt,
        expr_stmt,
        labelled_stmt,
        var_decl,
        type_decl);

    public rule block =
        seq(LBRACE, stmt.at_least(0), RBRACE)
        .push(xs -> Block.mk(list(xs)));

    public rule statements =
        stmt.at_least(0)
        .collect().as_list(Statement.class);

    /// TOP-LEVEL ==================================================================================

    public rule package_decl =
        seq(annotations, _package, qualified_iden, SEMI)
        .push(xs -> PackageDeclaration.mk($(xs,0), $(xs,1)));

    public rule import_decl =
        seq(_import, _static.as_bool(), qualified_iden, seq(DOT, STAR).as_bool(), SEMI)
        .push(xs -> ImportDeclaration.mk($(xs,0), $(xs,1), $(xs,2)));

    public rule import_decls =
        import_decl.at_least(0)
        .collect().as_list(ImportDeclaration.class);

    public rule root =
        seq(ws, package_decl.maybe(), import_decls, type_decls)
        .push(xs -> JavaFile.mk($(xs,0), $(xs,1), $(xs,2)));

    // =============================================================================================

    { make_rule_names(); }
}