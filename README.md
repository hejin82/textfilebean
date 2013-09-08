textfilebean
============

固定長／可変長（キャラクタ区切り）のテキストファイルをJavaBeanにマッピングさせる

使い方：
    CharacterReadProcessor<FooBean> processor = new CharacterReadProcessor<FooBean>();
    InitialLoader loader = new AnnotationLoader();
    FieldColumn[] fieldColumns = loader.load(FooBean.class);
    Reader reader = null;
    try  {
        reader = new BufferedReader(new InputStreamReader(
            new FileInputStream("sample/sample.txt"), "Windows-31J"));
        // 1st line.(テキストファイルの内容がFooBeanにマッピングされる。)
        // 繰り返し読んでいって、EOFの場合nullが返る。
        FooBean bean = processor.process(reader, FooBean.class, fieldColumns);
    } catch {
        // 省略
    } finally {
        // 省略
    }

読み取りたいテキストファイルがこんなんだったりする。(sample/sample.txt)

    123:1234567890:1234567890
    001:Michel    :Jordan    
    002:Michel    :Jacson    
    003:Vanessa   :Williams  
    004:名無しの      :権兵衛       

マッピングされるJavaBeanの書き方:
    class FooBean {
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
