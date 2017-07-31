package lsm.jvm;

import java.lang.ref.SoftReference;

/**
 * Created by lishenming on 2017/3/1.
 * 弱引用
 */
public class TestSoftReference {
    public static void main(String[] args){
        //强引用
        Test test = new Test();
        //弱引用
        SoftReference<Test> sr = new SoftReference<Test>(test);
        test = null;
        if(sr!=null){
            // 还没有被回收器回收，直接获取
            test = sr.get(); //test再次变为一个强引用
        } else{
            // 由于内存吃紧，所以对软引用的对象回收了
            test = new Test();
            // 重新构建
            sr = new SoftReference<Test>(test);
        }
    }
}

class Test{
    int[] a ;
    public Test(){
        a = new int[100000000];
    }
}
