package com.shawn;

/**
 * Author: Shawn Guo
 * E-mail:air_fighter@163.com
 *
 * Create Time: 2015/10/16 09:40
 * Last Modified Time: 2015/10/19 10:55
 *
 * Class Name: ICTCLASDemo
 * Class Function:
 *          This is the original NLPIR tokenizer, which is used to tokenize the words
 *          without any dictionary.
 */

import java.io.*;

import utils.SystemParas;
import java.util.Vector;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class ICTCLASDemo {

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

    public static void initLib() {
        int charset_type = 1;
        String argu = System.getProperty("user.dir");
        int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");

        if (init_flag == 0) {
            System.err.println("初始化失败！");
            System.exit(1);
        }
    }

    public static void importUserDict(String dictName)
                    throws IOException, ClassNotFoundException {
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
            System.out.println("读取文件失败！");
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

    public static void main(String[] args) throws Exception {
        try {
            initLib();
            String inputString = BasicIO.readFile2String("question.txt");
            importUserDict(System.getProperty("user.dir") + "\\data\\userDic.txt");

            String outputString = CLibrary.Instance.NLPIR_ParagraphProcess(inputString, 3);
            String[] tokens = outputString.split(" ", 0);
            Vector words = new Vector();
            Vector POSs = new Vector();
            for(int i = 0; i < tokens.length; i++) {
                String token = tokens[i].trim();
                if (token.length() >= 3) {
                        words.addElement(token.split("/")[0]);
                        POSs.addElement(token.split("/")[1]);
                }
            }

            System.out.println("分词结果为： " + outputString);
            System.out.println("分割结果为： ");
            for(int i = 0; i < words.size(); i++) {
                System.out.println(words.elementAt(i) + " | " + POSs.elementAt(i));
            }

            int nCountKey = 0;
            String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(inputString, 10,false);
            System.out.print("关键词提取结果是：" + nativeByte);

            CLibrary.Instance.NLPIR_Exit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
