package Package;
<<<<<<< HEAD

=======
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
import java.io.*;

public class Printtokens{
	static int error = 0;
	static int keyword = 1;
	static int spec_symbol = 2;
	static int identifier = 3;
	static int num_constant = 41;
	static int str_constant = 42;
	static int char_constant = 43;
	static int comment = 5;
	
	/***********************************************/
	/* NMAE:	open_character_stream          */
	/* INPUT:       a filename                     */
	/* OUTPUT:      a BufferedReader */
	/* DESCRIPTION: when not given a filename,     */
	/*              open stdin,otherwise open      */
	/*              the existed file               */
	/***********************************************/
	BufferedReader open_character_stream(String fname) {
		BufferedReader br = null;
		if (fname == null) { 
<<<<<<< HEAD
			br = new BufferedReader(new InputStreamReader(System.in));
=======
			//Suggested Change
			return new BufferedReader(new StringReader(""));
			//br = new BufferedReader(new InputStreamReader(System.in));
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
		} else {
			try {
				FileReader fr = new FileReader(fname);
				br = new BufferedReader(fr);
			} catch (FileNotFoundException e) {
				System.out.print("The file " + fname +" doesn't exists\n");
				e.printStackTrace();
			}
		}
		
		return br; 
	}
	
	/**********************************************/
	/* NAME:	get_char                      */
	/* INPUT:       a BufferedReader      */
	/* OUTPUT:      a character; when EOF, return -1              */
	/**********************************************/
	int get_char(BufferedReader br){ 
		int ch = 0;
	    try {
	    	br.mark(4); 
			ch= br.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return ch;
	}
	
	/***************************************************/
	/* NAME:      unget_char                           */
	/* INPUT:     a BufferedReader,a character */
	/* OUTPUT:    a character                          */
	/* DESCRIPTION:move backward.when unable to put back,return -1(EOF)  */
	/***************************************************/
	char unget_char (int ch,BufferedReader br) { 
	  try {
		br.reset();
	} catch (IOException e) {
		e.printStackTrace();
	}
		 return 0; 
	}
	
	/********************************************************/
	/* NAME:	open_token_stream                       */
	/* INPUT:       a filename                              */
	/* OUTPUT:      a BufferedReader             */
	/* DESCRIPTION: when filename is EMPTY,choice standard  */
	/*              input device as input source            */
	/********************************************************/
	BufferedReader open_token_stream(String fname)
	{
		BufferedReader br;
	if(fname==null || fname.equals("")) 
	    br=open_character_stream(null);
	 else
	    br=open_character_stream(fname);
	 return br;
	}
	
	/********************************************************/
	/* NAME :	get_token                               */
	/* INPUT: 	a BufferedReader          */
	/* OUTPUT:      a token string                                */
	/* DESCRIPTION: according the syntax of tokens,dealing  */
	/*              with different case  and get one token  */
	/********************************************************/
	String get_token(BufferedReader br)
	{ 
<<<<<<< HEAD
	  int i=0,j;
=======
	  //int i=0,j;  //Not needed
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	  int id=0;
	  int res = 0;
	  char ch = '\0';
	 
	  StringBuilder sb = new StringBuilder();

<<<<<<< HEAD
	   try {
		   res = get_char(br);
		   if (res == -1) {
			   return null;
		   }
		   ch = (char)res;
		  while(ch==' '||ch=='\n' || ch == '\r')   
	      {
			res = get_char(br);
			ch = (char)res;
	      } 
	   
	   if(res == -1)return null;
	   sb.append(ch);
	   if(is_spec_symbol(ch)==true)return sb.toString(); 
	   if(ch =='"')id=2;    /* prepare for string */ 
       if(ch ==59)id=1;    /* prepare for comment */ 
	   
	   res = get_char(br);
	   if (res == -1) {
		   unget_char(ch,br);
		   return sb.toString();
	   }
	   ch = (char)res;

	   while (is_token_end(id,res) == false)/* until meet the end character */
	   {
	       sb.append(ch);
	       br.mark(4);
	       res = get_char(br);
		   if (res == -1) {
			   break;
		   }
		   ch = (char)res;
	   }
	 
	   if(res == -1)       /* if end character is eof token    */
	      { unget_char(ch,br);        /* then put back eof on token_stream */
	        return sb.toString();
	      }
	 
	   if(is_spec_symbol(ch)==true)     /* if end character is special_symbol */
	      { unget_char(ch,br);        /* then put back this character       */
	        return sb.toString();
	      }
	   if(id==1)                  /* if end character is " and is string */
	     {
		   if (ch == '"') {
			   sb.append(ch);
		   }
	       return sb.toString(); 
	     }
	   if(id==0 && ch==59)
	                                   /* when not in string or comment,meet ";" */
	     { unget_char(ch,br);       /* then put back this character         */
	       return sb.toString(); 
	     }
=======
		try {
			res = get_char(br);
			if (res == -1) {
				return null;
			}
			ch = (char)res;
			while(ch==' '||ch=='\n' || ch == '\r')   
			{
				res = get_char(br);
				ch = (char)res;
			} 

		
		if(res == -1)return null; //Included in suggested change
		sb.append(ch);
		if(is_spec_symbol(ch)==true)return sb.toString(); 
		if(ch =='"')id=2;    /* prepare for string */ 
		if(ch ==59)id=1;    /* prepare for comment */ 
		
		res = get_char(br);
		if (res == -1) {
			unget_char(ch,br);
			return sb.toString();
		}
		ch = (char)res;

		while (is_token_end(id,res) == false)/* until meet the end character */
		{
			sb.append(ch);
			br.mark(4);
			res = get_char(br);
			if (res == -1) {
				break;
			}
			ch = (char)res;
		}
		
		if(res == -1)       /* if end character is eof token    */
			{ 
			unget_char(ch,br);        /* then put back eof on token_stream */
			return sb.toString();
			}
		
		if(is_spec_symbol(ch)==true)     /* if end character is special_symbol */
			{ 
			unget_char(ch,br);        /* then put back this character       */
			return sb.toString();
			}
		if(id==1)                  /* if end character is " and is string */
			{
			if (ch == '"') {
				sb.append(ch);
			}
			return sb.toString(); 
			}
		if(id==0 && ch==59)
										/* when not in string or comment,meet ";" */
			{ unget_char(ch,br);       /* then put back this character         */
			return sb.toString(); 
			}
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	} catch (IOException e) {
		e.printStackTrace();
	}
	   
	   return sb.toString();                   /* return nomal case token             */
	}
	
	/*******************************************************/
	/* NAME:	is_token_end                           */
	/* INPUT:       a character,a token status             */
	/* OUTPUT:	a BOOLEAN value                        */
	/*******************************************************/
	static boolean is_token_end(int str_com_id, int res)
	{
	 if(res==-1)return(true); /* is eof token? */
	 char ch = (char)res;
	 if(str_com_id==1)          /* is string token */
	    { if(ch=='"' || ch=='\n' || ch == '\r' || ch=='\t')   /* for string until meet another " */ 
	         return true;
	      else
	         return false;
	    }

	 if(str_com_id==2)    /* is comment token */
	   { if(ch=='\n' || ch == '\r' || ch=='\t')     /* for comment until meet end of line */ 
	        return true;
	      else
	        return false;
	   }

	 if(is_spec_symbol(ch)==true) return true; /* is special_symbol? */
	 if(ch ==' ' || ch=='\n'|| ch=='\r' || ch==59) return true; 
	                              /* others until meet blank or tab or 59 */
	 return false;               /* other case,return FALSE */
	}
	
	/****************************************************/
	/* NAME :	token_type                          */
	/* INPUT:       a token              */
	/* OUTPUT:      an integer value                    */
	/* DESCRIPTION: the integer value is corresponding  */
	/*              to the different token type         */
	/****************************************************/
	static int token_type(String tok)
	{ 
	 if(is_keyword(tok))return(keyword);
	 if(is_spec_symbol(tok.charAt(0)))return(spec_symbol);
	 if(is_identifier(tok))return(identifier);
	 if(is_num_constant(tok))return(num_constant);
	 if(is_str_constant(tok))return(str_constant);
<<<<<<< HEAD
	 if(is_char_constant(tok))return(char_constant);
	 if(is_comment(tok))return(comment);
=======
	 if(is_comment(tok))return(comment);                        //<----------
	 if(is_char_constant(tok))return(char_constant);
	 //if(is_comment(tok))return(comment);                      ////////////////Changed Order
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	 return(error);                    /* else look as error token */
	}
	
	/****************************************************/
	/* NAME:	print_token                             */
	/* INPUT:	a token                                 */
	/****************************************************/
	void print_token(String tok)
	{ int type;
	  type=token_type(tok);
	 if(type==error)
	   { 
	   	System.out.print("error,\"" + tok + "\".\n");
	   }
	   
	 if(type==keyword)
	   {
	   System.out.print("keyword,\"" + tok + "\".\n");
	   }
	  
	 if(type==spec_symbol)print_spec_symbol(tok);
	 if(type==identifier)
	   {
	   System.out.print("identifier,\"" + tok + "\".\n");
	   }
	 if(type==num_constant)
	   {
	   System.out.print("numeric," + tok + ".\n");
	   }
	 if(type==str_constant)
	   {
	   System.out.print("string," + tok + ".\n");
	   }	 
	 if(type==char_constant)
	   {
	    System.out.print("character,\"" + tok.charAt(1) + "\".\n");
	   }
	 if(type==comment)
	   {
	    System.out.print("comment,\"" + tok + "\".\n");
	   }
   }

	/* the code for tokens judgment function */

	
	/*************************************/
	/* NAME:	is_comment           */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_comment(String ident)
	{
	  if( ident.charAt(0) ==59 )   /* the char is 59   */
	     return true;
	  else
	     return false;
	}
	
	/*************************************/
	/* NAME:	is_keyword           */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_keyword(String str)
	{ 
	 if (str.equals("and") || str.equals("or") || str.equals("if") ||
			 str.equals("xor")||str.equals("lambda")||str.equals("=>"))
	      return true;
	  else 
	      return false;
	}
	
	/*************************************/
	/* NAME:	is_char_constant     */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_char_constant(String str)
	{
<<<<<<< HEAD
		// if (str.length() > 2 || str.charAt(0)=='#' && Character.isLetter(str.charAt(1)))
		// char consts must be exactly length 2
	  if (str.length() == 2 && str.charAt(0)=='#' && Character.isLetter(str.charAt(1))) 
=======
	  if (str.length() > 2 || str.charAt(0)=='#' && Character.isLetter(str.charAt(1))) 
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	     return true;
	  else  
	     return false;
	}
	
	/*************************************/
	/* NAME:	is_num_constant      */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_num_constant(String str)
	{
	  int i=1;
	  
	  if ( Character.isDigit(str.charAt(0))) 
	    {
		while ( i < str.length() && str.charAt(i) != '\0' )  
	      {
<<<<<<< HEAD
			// if(Character.isDigit(str.charAt(i+1))
			// Cannot access i+1 before verifying its exisitance
		   if(Character.isDigit(str.charAt(i)))
=======
		   if(Character.isDigit(str.charAt(i)))      ////////////////Removed +1
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	         i++;
	       else
	         return false;
	      }                         /* end WHILE */
	    return true;
	    }
	  else
	   return false;               /* other return FALSE */
	}
	
	/*************************************/
	/* NAME:	is_str_constant      */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_str_constant(String str)
	{
	  int i=1;
	 
<<<<<<< HEAD
	//   if ( str.charAt(0) =='"')
	// Need to check if last char is " and that string is not empty
	if (str.length() >= 3 && str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"')
	    //  { while (i < str.length() && str.charAt(i)!='\0') 
		// Check there are not quotes in the middle of the string
		 { while (i < str.length() - 1 && str.charAt(i)!='\0') 
	         { if(str.charAt(i)=='"' )
	             return false;        /* meet an intermediate '"'           */
=======
	  if ( str.charAt(0) =='"')
	     { while (i < str.length() && str.charAt(i)!='\0') 
	         { if(str.charAt(i)=='"' )
	             return true;        /* meet the second '"'           */
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	           else
	           i++;
	         }               /* end WHILE */
	     return true;	
	    }
	  else
	    return false;       /* other return FALSE */
	}
	
	/*************************************/
	/* NAME:	is_identifier         */
	/* INPUT: 	a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_identifier(String str)
	{
	  int i=1; 

	  if ( Character.isLetter(str.charAt(0)) ) 
	     {
	        while(i < str.length() && str.charAt(i) !='\0' )   /* unti meet the end token sign */
	           { 
	            if(Character.isLetter(str.charAt(i)) || Character.isDigit(str.charAt(i)))   
	               i++;
	            else
	               return false;
	           }      /* end WHILE */
<<<<<<< HEAD
		    // return false; 
			return true; 
	     }
	  else
	  // return true; 
		return false; 
=======
		    return true;  ///////////////Flipped
	     }
	  else
		return false; ///////////Flipped

>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	}
	
	
	
	/*************************************************/
	/* NAME:        print_spec_symbol                */
	/* INPUT:       a spec_symbol token */
	/* OUTPUT :     print out the spec_symbol token  */
	/*              according to the form required   */
	/*************************************************/
	static void print_spec_symbol(String str)
<<<<<<< HEAD
	{	// if      (str.equals(")"))
		// wrong parenthase
		if      (str.equals("(")) 
=======
	{
		if      (str.equals("("))          //////////////Symbol ) was used twice. changed to (
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	    {
	         
	             System.out.print("lparen.\n");
	             return;
	    } 
	    if (str.equals(")"))
	    {
	      
	             System.out.print("rparen.\n");
	             return;
	    }
	    if (str.equals("["))
	    {
	             System.out.print("lsquare.\n");
	             return;
	    }
	    if (str.equals("]"))
	    {
	       
	             System.out.print("rsquare.\n");
	             return;
	    }
	    if (str.equals("'"))
	    {
	             System.out.print("quote.\n");
	             return;
	    }
	    if (str.equals("`"))
	    {
	 
	             System.out.print("bquote.\n");
	             return;
	    }
	    
	    if (str.equals(","))
	    {	 
	             System.out.print("comma.\n");
	             return;
	    }
	}
	
	/*************************************/
	/* NAME:        is_spec_symbol       */
	/* INPUT:       a token */
	/* OUTPUT:      a BOOLEAN value      */
	/*************************************/
	static boolean is_spec_symbol(char c)
	{
	    if (c == '(')
	    {  
	        return true;
	    }
	    if (c == ')')
	    {
	        return true;
	    }
	    if (c == '[')
	    {
	        return true;
	    }
	    if (c == ']')
	    {
	        return true;
	    }
	    if (c == '/') 
	    {
	        return true;
	    }
	    if (c == '`')
	    {
	        return true;
	    }
	    if (c == ',')
	    {
	        return true;
	    }
<<<<<<< HEAD
		// Missing " and '
		if (c == '"') 
	    {
	        return true;
	    }
		if (c == '\'') 
	    {
	        return true;
	    }
=======
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
	    return false;     /* others return FALSE */
	}
	
	public static void main(String[] args)  {
		String fname = null;
		if (args.length == 0) {	/* if not given filename,take as '""' */
			fname = new String();
		} else if (args.length == 1) {
			fname = args[0]; 
		} else {
			System.out.print("Error! Please give the token stream\n");
			System.exit(0);
		}
		Printtokens t = new Printtokens();
		BufferedReader br = t.open_token_stream(fname);	/* open token stream */
<<<<<<< HEAD

		// Added for user clarity
		if (fname == null) {
            System.out.println("Please type tokens to classify (press Ctrl+D to end):");
        }

=======
>>>>>>> 5fbd02651574503e3e96cdcb215b8221435ca685
		String tok = t.get_token(br);
		while (tok != null) {	/* take one token each time until eof */
			t.print_token(tok);
			tok = t.get_token(br);
			
		}
		
		System.exit(0);
	}
}