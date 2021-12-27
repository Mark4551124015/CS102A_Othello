package util;

import newData.Operation;
import newData.intVct;
import object.Player;

import java.io.*;
import java.util.ArrayList;

public class Tools {

    private static String path = "./save/";

    //存Json
    public static void saveDataToFile(String fileName,String data) {
        BufferedWriter writer = null;
        File file = new File(path + fileName + ".json");
        //如果文件不存在，则新建一个
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");
    }

    //取Json
    public static String getStringFromFile(String fileName) {
        String Path= path + fileName+ ".json";
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    //删除文件
    public static void delete(String fileName) {
        File file = new File( fileName);
        System.out.println(file.getPath());

        System.out.print(file.getName());
        if (!file.exists()) return;
        if (file.isFile() || file.list() == null) {
            file.delete();
            System.out.println("删除了" + file.getName());
        } else {
            File[] files = file.listFiles();
            for (File a : files) {
                delete(a.getName());
            }
            file.delete();
            System.out.println("删除了" + file.getName());
        }

    }

    //删除数组中元素
    public static void removePlayer(ArrayList<Player> list, Player object) {
        for(int i=0;i<list.size();i++) {
            if(object.equals(list.get(i))) {
                list.remove(i);
                --i;//删除了元素，迭代的下标也跟着改变
                }
        }
    }
    public static void removeOperation(ArrayList<Object> list, Operation object) {
        for(int i=0;i<list.size();i++) {
            if(object.equals(list.get(i))) {
                list.remove(i);
                --i;//删除了元素，迭代的下标也跟着改变
            }
        }
    }

    public static void removeString(ArrayList<String> list, String object) {
        for(int i=0;i<list.size();i++) {
            if(object.equals(list.get(i))) {
                list.remove(i);
                --i;//删除了元素，迭代的下标也跟着改变
            }
        }
    }


    //清理重复的项
    public static void cleanArray(ArrayList<intVct> object) {
        for (int a = 0; a < object.size(); a++) {
            for (int b = 0; b < a; b++) {
                if (object.get(b).r == object.get(a).r && object.get(b).c == object.get(a).c) {
                    object.remove(b);
                    --a;
                }
            }
        }
    }

    public static int getGreatestIndex(ArrayList<Integer> arrayList){
        int greatest = 0;
        for (int index = 0; index < arrayList.size();++index) {
            if (arrayList.get(index) > arrayList.get(greatest)) {
                greatest = index;
            }
        }
        return greatest;
    }

    public static int getSmallestIndex(ArrayList<Integer> arrayList){
        int smallest = 0;
        for (int index = 0; index < arrayList.size();++index) {
            if (arrayList.get(index) < arrayList.get(smallest)) {
                smallest = index;
            }
        }
        return smallest;
    }


}
