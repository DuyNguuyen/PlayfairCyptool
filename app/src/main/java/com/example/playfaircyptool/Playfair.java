package com.example.playfaircyptool;

import android.graphics.Point;
import android.text.TextUtils;

public class Playfair {
    //length of digraph array
    private int length = 0;
    private String key;
    private String input;
    private String [][] table;

    public Playfair(String key, String input) {
        this.key = key;
        this.input = input;
        table = this.createKeyMatrix();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean isValidKey(){
        return !TextUtils.isEmpty(key);
    }

    public boolean isValidInput(){
        return !TextUtils.isEmpty(input);
    }

    public String getBigrams(String str){
        String bigrams = str;
        bigrams = bigrams.replaceAll("[^A-Z]","");
        bigrams = bigrams.replace("J","I");
        return bigrams;
    }

    public String[][] createKeyMatrix()
    {
        String[][] keyMatrix = new String[5][5];
        String keyString = key + "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        //Gán các chuỗi rỗng vào ma trận
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                keyMatrix[i][j] = "";
        for(int k = 0; k < keyString.length(); k++)
        {
            boolean repeat = false;
            boolean used = false;
            for(int i = 0; i < 5; i++)
            {
                for(int j = 0; j < 5; j++)
                {
                    if(keyMatrix[i][j].equals("" + keyString.charAt(k)))
                    {
                        repeat = true;
                    }
                    else if(keyMatrix[i][j].equals("") && !repeat && !used)
                    {
                        keyMatrix[i][j] = "" + keyString.charAt(k);
                        used = true;
                    }
                }
            }
        }
        return keyMatrix;
    }

    //cipher: encodes input, and returns the output
    public String cipher()
    {
        length = (int) input.length() / 2 + input.length() % 2;
        //insert x between double-letter digraphs & redefines "length"

        for(int i = 0; i < (length - 1); i++)
        {
            if(input.charAt(2 * i) == input.charAt(2 * i + 1))
            {
                input = new StringBuffer(input).insert(2 * i + 1, 'X').toString();
                length = (int) input.length() / 2 + input.length() % 2;
            }
        }
        //------------makes plaintext of even length--------------
        //creates an array of digraphs
        String[] digraph = new String[length];
        //loop iterates over the plaintext
        for(int j = 0; j < length ; j++)
        {
        //checks the plaintext is of even length or not
            if(j == (length - 1) && input.length() / 2 == (length - 1))
        //if not addends X at the end of the plaintext
                input = input + "X";
            digraph[j] = input.charAt(2 * j) +""+ input.charAt(2 * j + 1);
        }
        //encodes the digraphs and returns the output
        String out = "";
        String[] encDigraphs = new String[length];
        encDigraphs = encodeDigraph(digraph);
        for(int k = 0; k < length; k++)
            out = out + encDigraphs[k];
        return out;
    }
    //---------------encryption logic-----------------
//encodes the digraph input with the cipher's specifications
    private String[] encodeDigraph(String di[])
    {
        String[] encipher = new String[length];
        for(int i = 0; i < length; i++)
        {
            char a = di[i].charAt(0);
            char b = di[i].charAt(1);
            int r1 = (int) getPoint(a).x;
            int r2 = (int) getPoint(b).x;
            int c1 = (int) getPoint(a).y;
            int c2 = (int) getPoint(b).y;
            //executes if the letters of digraph appear in the same row
            //in such case shift columns to right
            if(r1 == r2)
            {
                c1 = (c1 + 1) % 5;
                c2 = (c2 + 1) % 5;
            }
            //executes if the letters of digraph appear in the same column
            //in such case shift rows down
            else if(c1 == c2)
            {
                r1 = (r1 + 1) % 5;
                r2 = (r2 + 1) % 5;
            }
            //executes if the letters of digraph appear in the different row and different column
            //in such case swap the first column with the second column
            else
            {
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }
            //performs the table look-up and puts those values into the encoded array
            encipher[i] = table[r1][c1] + "" + table[r2][c2];
        }
        return encipher;
    }

    //-----------------------decryption logic---------------------
    // decodes the output given from the cipher and decode methods (opp. of encoding process)
    public String decode()
    {
        String decoded = "";
        for(int i = 0; i < input.length() / 2; i++)
        {
            char a = input.charAt(2*i);
            char b = input.charAt(2*i+1);
            int r1 = (int) getPoint(a).x;
            int r2 = (int) getPoint(b).x;
            int c1 = (int) getPoint(a).y;
            int c2 = (int) getPoint(b).y;
            if(r1 == r2)
            {
                c1 = (c1 + 4) % 5;
                c2 = (c2 + 4) % 5;
            }
            else if(c1 == c2)
            {
                r1 = (r1 + 4) % 5;
                r2 = (r2 + 4) % 5;
            }
            else
            {
        //swapping logic
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }
            decoded = decoded + table[r1][c1] + table[r2][c2];
        }
        //returns the decoded message
        return decoded;
    }
    // returns a point containing the row and column of the letter
    private Point getPoint(char c)
    {
        Point pt = new Point(0,0);
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                if(c == table[i][j].charAt(0))
                    pt = new Point(i,j);
        return pt;
    }
}
