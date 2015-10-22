package com.shawn;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 * Author:             Shawn Guo
 * E-mail:             air_fighter@163.com
 *
 * Create Time:        2015/10/20 09:54
 * Last Modified Time: 2015/10/20 10:54
 *
 * Class Name:         Options
 * Class Function:
 *                     This class implements one of the options in the input.
 *                     It contains tokens, taggers, words, and a HashMap from taggers to words.
 *
 * Note:
 *                     The encapsulation should be improved later.
 */

public class Options {
    public  String inputString = null;
    public String[]tokens = null;
    public Vector<String> words = new Vector<>();
    public Vector<String> taggers = new Vector<>();
    public HashMap<String, Vector<String>> tagger2Word = new HashMap<>();
    public HashSet<String> relatedConcepts = new HashSet<>();

    public String outputString = null;

    Options(String input) {
        try {

            initLib();
            inputString = input;

            importUserDict(System.getProperty("user.dir") + "\\data\\userDic.txt");

            tokenizeAndTag();

            //outputResult();

            CLibrary.Instance.NLPIR_Exit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 定义接口CLibrary，继承自com.sun.jna.Library
    public interface CLibrary extends Library {
        // 定义并初始化接口的静态变量
        CLibrary Instance = (CLibrary) Native.loadLibrary(
                System.getProperty("user.dir") + "\\NLPIR", CLibrary.class);

        public int NLPIR_Init(String sDataPath, int encoding, String sLicenceCode);

        public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

        public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);

        public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);

        public int NLPIR_AddUserWord(String sWord);

        public int NLPIR_DelUsrWord(String sWord);

        public int NLPIR_ImportUserDict(String sFilename);

        public String NLPIR_GetLastErrorMsg();

        public void NLPIR_Exit();
    }

    public  void initLib() {
        int charset_type = 1;
        String argu = System.getProperty("user.dir");
        int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");

        if (init_flag == 0) {
            System.err.println("Options initLib: 初始化失败！");
            System.exit(1);
        }
    }

    public  void importUserDict(String dictName) throws IOException, ClassNotFoundException {
        File inputFile = new File(dictName);
        FileInputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String lineString = null;

        try {
            inputStream = new FileInputStream(inputFile);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            while ( (lineString = bufferedReader.readLine()) != null) {
                CLibrary.Instance.NLPIR_AddUserWord(lineString);
            }
        }  catch (IOException e) {
            System.out.println("Options importUserDict: 读取文件失败！");
            System.exit(1);
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void tokenizeAndTag() throws Exception {
        outputString = CLibrary.Instance.NLPIR_ParagraphProcess(inputString, 3);
        tokens = outputString.split(" ", 0);
        words = new Vector();
        taggers = new Vector();

        for(int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.length() >= 3) {
                words.addElement(token.split("/")[0]);
                taggers.addElement(token.split("/")[1]);
                if (!tagger2Word.containsKey(taggers.lastElement())) {
                    Vector<String> vec = new Vector<>();
                    vec.addElement(token.split("/")[0]);
                    tagger2Word.put(taggers.lastElement(), vec);
                }
                else {
                    tagger2Word.get(taggers.lastElement()).addElement(token.split("/")[0]);
                }
            }
        }
    }

    public  void outputResult() {
        System.out.println("分词结果为： " + outputString);
        System.out.println("分割结果为： ");
        for(int i = 0; i < words.size(); i++) {
            System.out.println(words.elementAt(i) + " | " + taggers.elementAt(i));
        }

        String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(inputString, 10,false);
        System.out.println("关键词提取结果是：" + nativeByte);
        System.out.println(tagger2Word);
    }
}
