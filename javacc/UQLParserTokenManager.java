/* Generated By:JavaCC: Do not edit this line. UQLParserTokenManager.java */
package javacc;
import java.io.StringReader;
import java.io.Reader;
import java.io.File;
import java.lang.String;
import java.io.BufferedReader;
import java.io.FileReader;

/** Token Manager. */
public class UQLParserTokenManager implements UQLParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x700L) != 0L)
         {
            jjmatchedKind = 26;
            return 0;
         }
         if ((active0 & 0x186f860L) != 0L)
         {
            jjmatchedKind = 26;
            return 7;
         }
         if ((active0 & 0x8L) != 0L)
            return 5;
         if ((active0 & 0x10000L) != 0L)
            return 7;
         return -1;
      case 1:
         if ((active0 & 0x1863120L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 1;
            return 7;
         }
         if ((active0 & 0xce40L) != 0L)
            return 7;
         return -1;
      case 2:
         if ((active0 & 0x1800000L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 2;
            return 7;
         }
         if ((active0 & 0x63120L) != 0L)
            return 7;
         return -1;
      case 3:
         if ((active0 & 0x800000L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 3;
            return 7;
         }
         if ((active0 & 0x1000000L) != 0L)
            return 7;
         return -1;
      case 4:
         if ((active0 & 0x800000L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 4;
            return 7;
         }
         return -1;
      case 5:
         if ((active0 & 0x800000L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 5;
            return 7;
         }
         return -1;
      case 6:
         if ((active0 & 0x800000L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 6;
            return 7;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 10:
         return jjStopAtPos(0, 4);
      case 13:
         return jjStartNfaWithStates_0(0, 3, 5);
      case 33:
         return jjMoveStringLiteralDfa1_0(0x400000L);
      case 40:
         return jjStopAtPos(0, 19);
      case 41:
         return jjStopAtPos(0, 20);
      case 61:
         return jjStopAtPos(0, 21);
      case 97:
         return jjMoveStringLiteralDfa1_0(0x20L);
      case 99:
         return jjMoveStringLiteralDfa1_0(0x800000L);
      case 101:
         return jjStartNfaWithStates_0(0, 16, 7);
      case 111:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x40000L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x700L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x102f800L);
      default :
         return jjMoveNfa_0(1, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 61:
         if ((active0 & 0x400000L) != 0L)
            return jjStopAtPos(1, 22);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x1001100L);
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x40000L);
      case 108:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(1, 10, 7);
         else if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(1, 15, 7);
         break;
      case 109:
         return jjMoveStringLiteralDfa2_0(active0, 0x2000L);
      case 110:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(1, 11, 7);
         return jjMoveStringLiteralDfa2_0(active0, 0x20L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x820000L);
      case 114:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(1, 6, 7);
         break;
      case 116:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(1, 9, 7);
         else if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(1, 14, 7);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 97:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(2, 8, 7);
         else if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(2, 12, 7);
         else if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(2, 13, 7);
         break;
      case 100:
         if ((active0 & 0x20L) != 0L)
            return jjStartNfaWithStates_0(2, 5, 7);
         else if ((active0 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(2, 18, 7);
         break;
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000000L);
      case 105:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(2, 17, 7);
         break;
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 110:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(3, 24, 7);
         break;
      case 116:
         return jjMoveStringLiteralDfa4_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa5_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 105:
         return jjMoveStringLiteralDfa6_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 110:
         return jjMoveStringLiteralDfa7_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 115:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(7, 23, 7);
         break;
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 12;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 7:
                  if ((0x3ff280000000000L & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAdd(7);
                  break;
               case 1:
                  if ((0x3ff280000000000L & l) != 0L)
                  {
                     if (kind > 26)
                        kind = 26;
                     jjCheckNAdd(7);
                  }
                  else if ((0x2400L & l) != 0L)
                  {
                     if (kind > 25)
                        kind = 25;
                  }
                  else if (curChar == 34)
                     jjCheckNAddTwoStates(9, 11);
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 4:
                  if ((0x2400L & l) != 0L && kind > 25)
                     kind = 25;
                  break;
               case 5:
                  if (curChar == 10 && kind > 25)
                     kind = 25;
                  break;
               case 6:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 8:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(9, 11);
                  break;
               case 9:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(9, 10);
                  break;
               case 10:
                  if (curChar == 34 && kind > 27)
                     kind = 27;
                  break;
               case 11:
                  if (curChar == 34 && kind > 28)
                     kind = 28;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 26)
                        kind = 26;
                     jjCheckNAdd(7);
                  }
                  if (curChar == 110)
                  {
                     if (kind > 7)
                        kind = 7;
                  }
                  break;
               case 1:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 26)
                        kind = 26;
                     jjCheckNAdd(7);
                  }
                  if (curChar == 83)
                     jjstateSet[jjnewStateCnt++] = 2;
                  else if (curChar == 115)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 2:
                  if (curChar == 78 && kind > 7)
                     kind = 7;
                  break;
               case 3:
                  if (curChar == 83)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 7:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAdd(7);
                  break;
               case 9:
                  jjAddStates(0, 1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 9:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(0, 1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 12 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   9, 10, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, "\141\156\144", "\157\162", null, "\163\150\141", 
"\163\164", "\163\154", "\164\156", "\164\150\141", "\164\155\141", "\164\164", 
"\164\154", "\145", "\164\157\151", "\160\151\144", "\50", "\51", "\75", "\41\75", 
"\143\157\156\164\141\151\156\163", "\164\150\145\156", null, null, null, null, };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x1fffffe1L, 
};
static final long[] jjtoSkip = {
   0x1eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[12];
private final int[] jjstateSet = new int[24];
protected char curChar;
/** Constructor. */
public UQLParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public UQLParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 12; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100000200L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

}
