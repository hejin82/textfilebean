textfilebean
============

固定長／可変長（キャラクタ区切り）のテキストファイルをJavaBeansにマッピングさせる

 * 対象年齢:3歳以上
 * 対象バージョン:Java 6以上
 * 依存ライブラリ:なし
 * ライセンス:Apache License 2.0 http://www.apache.org/licenses/LICENSE-2.0.txt

使い方：target/textfilebean-x.x.x.jarをクラスパスに通す。

    AnnotationBeanReaderFactory<FooBean> factory = new AnnotationBeanReaderFactory<FooBean>();
    Reader reader = null;
    try  {
        reader = new BufferedReader(new InputStreamReader(
            new FileInputStream("sample/sample.txt"), "Windows-31J"));
        RecordReader<FooBean> beanReader = factory.createReader(reader);
        // テキストファイルの内容がFooBeanにマッピングされる。
        // 繰り返し読んでいって、EOFの場合nullが返る。
        FooBean bean = beanReader.readLine();
    } catch {
        // 省略
    } finally {
        // 省略。でもJava 6の場合はちゃんとクローズしましょう。
    }

読み取りたいテキストファイルがこんなんだったりする。(sample/sample.txt)
全角テキストもchar一文字と認識される。

    123:1234567890:1234567890
    001:Michael   :Jordan    
    002:Michael   :Jackson    
    003:Vanessa   :Williams  
    004:名無しの      :権兵衛       

マッピングされるJavaBeansの書き方:

    public class FooBean {
        // ファイルから読み取られる順番でフィールドにアノテーションをつける。
        @IntegerColumn(length=3) private int i;
        @CharColumn private char delim1;
        @StringColumn(length=10, strict=true) private String user;
        @CharColumn private char delim2;
        @StringColumn(length=10) private String name;
        @StringColumn(length=1) private String terminal;
        
        public int getI() {
            return i;
        }
        public void setI(int i) {
            this.i = i;
        }
        public char getDelim1() {
            return delim1;
        }
        public void setDelim1(char delim1) {
            this.delim1 = delim1;
        }
        public String getUser() {
            return user;
        }
        public void setUser(String user) {
            this.user = user;
        }
        public char getDelim2() {
            return delim2;
        }
        public void setDelim2(char delim2) {
            this.delim2 = delim2;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getTerminal() {
            return terminal;
        }
        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }
    }

アノテーションの種類:

  <table>
    <tr>
      <th>アノテーション</th><th>属性</th>
    </tr>
    <tr>
      <td>CharColumn</td><td>なし。キャラクタ一文字だけ。charフィールドに付与してね。</td>
    </tr>
    <tr>
      <td>DelimCharColumn</td><td>可変長の区切り文字をdelimで指定(必須)。Stringのフィールドに付与してね。</td>
    </tr>
    <tr>
      <td>IntegerColumn</td><td>固定桁整数型。桁数をlengthで指定(必須)。尻切れになったら困る場合はstrict=trueを指定する。intフィールドに付与してね。</td>
    </tr>
    <tr>
      <td>StringColumn</td><td>固定桁文字列型。桁数をlengthで指定(必須)。尻切れになったら困る場合はstrict=trueを指定する。String(ry</td>
    </tr>
  </table>

今後やりたいこと:

 * 型を増やす
 * 日付型、数値フォーマッタ
 * BeanValidation(JSR-303)
 * charじゃなくてバイト列でも使いたい
 * 既存のJavaBeansに迷惑をかけるので、アノテーションによる設定を外だし。
