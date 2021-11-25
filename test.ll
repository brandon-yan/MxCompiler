string


define retType: void
parameters:  @g_init()
define retType: i32
parameters:  @main()
define retType: void
parameters: i32* @selection_sort(i32*)

define retType: void
parameters:  @g_init(){
%g_init.entry
    jump %g_init.entry

    return void


%g_init.exit

}

define retType: i32
parameters:  @main(){
%main.entry
    call define retType: void
parameters:  @g_init()

    @ n.addr55 allocate i32*

    @ funccall56 = call define retType: i32
parameters:  @g_getInt()

    store i32 @ funccall56 i32* @ n.addr55

    @ a.addr57 allocate i32**

    @ load58 = load i32 i32* @ n.addr55

    @ new_size159 = binary mul i32 @ load58 4

    @ new_size260 = binary add i32 @ load58 4

    @ call_malloc61 = call define retType: i8*
parameters:  @g_malloc(i32)

    store i32 @ load58 i32* @ call_malloc61

    @ array_head62 = GEP i32 i32* @ call_malloc61, i32 1

    @ array_head_cast63 = bitcast i32* @ array_head62 toi32*

    store i32* @ array_head_cast63 i32** @ a.addr57

    @ i.addr64 allocate i32*

    jump %for_init_block12


%for_init_block12
    @ load65 = load i32 i32* @ i.addr64

    store not ptr i32

    jump %for_cond_block13


%for_cond_block13
    @ load66 = load i32 i32* @ i.addr64

    @ load67 = load i32 i32* @ n.addr55

    @ slt68 = icmp slt i1 @ load66 @ load67

    branch @ slt68 true: %for_suite_block16 false: %for_dest_block15


%for_suite_block16
    @ load69 = load i32* i32** @ a.addr57

    @ load70 = load i32 i32* @ i.addr64

    @ getElementPtr71 = GEP i32 i32* @ load69, i32 @ load70

    @ GEP_Load72 = load i32 i32* @ getElementPtr71

    @ funccall73 = call define retType: i32
parameters:  @g_getInt()

    store not ptr i32

    jump %for_incr_block14


%for_incr_block14
    @ load74 = load i32 i32* @ i.addr64

    @ prefixadd75 = binary add i32 @ load74 1

    store i32 @ prefixadd75 i32* @ i.addr64

    jump %for_cond_block13


%for_dest_block15
    @ load76 = load i32* i32** @ a.addr57

    call define retType: void
parameters: i32* @selection_sort(i32*)

    jump %for_init_block17


%for_init_block17
    @ load77 = load i32 i32* @ i.addr64

    store not ptr i32

    jump %for_cond_block18


%for_cond_block18
    @ load78 = load i32 i32* @ i.addr64

    @ load79 = load i32 i32* @ n.addr55

    @ slt80 = icmp slt i1 @ load78 @ load79

    branch @ slt80 true: %for_suite_block21 false: %for_dest_block20


%for_suite_block21
    @ load82 = load i32* i32** @ a.addr57

    @ load83 = load i32 i32* @ i.addr64

    @ getElementPtr84 = GEP i32 i32* @ load82, i32 @ load83

    @ GEP_Load85 = load i32 i32* @ getElementPtr84

    @ funccall81 = call define retType: i8*
parameters:  @g_toString(i32)

    @ ConstString86 = GEP [1 x i8] [1 x i8]*  , i32 0, i32 0

    @ add87 = call define retType: i8*
parameters:  @g_stringAdd(i8*, i8*)

    call define retType: void
parameters:  @g_print(i8*)

    jump %for_incr_block19


%for_incr_block19
    @ load88 = load i32 i32* @ i.addr64

    @ prefixadd89 = binary add i32 @ load88 1

    store i32 @ prefixadd89 i32* @ i.addr64

    jump %for_cond_block18


%for_dest_block20
    @ ConstString90 = GEP [0 x i8] [0 x i8]* , i32 0, i32 0

    call define retType: void
parameters:  @g_println(i8*)

    @ mainret_val54 move 0

    jump %main.exit

    jump %main.exit


%main.exit
    return i32 @ mainret_val54


}

define retType: void
parameters: i32* @selection_sort(i32*){
%selection_sort.entry
    @ a.addr0 allocate i32**

    store i32* @ a i32** @ a.addr0

    @ i.addr2 allocate i32*

    @ n.addr3 allocate i32*

    @ load4 = load i32* i32** @ a.addr0

    @ array_size5 = GEP i32 i32* @ load4, i32 -1

    @ array_size_load6 = load i32 i32* @ array_size5

    store i32 @ array_size_load6 i32* @ n.addr3

    jump %for_init_block0


%for_init_block0
    @ load7 = load i32 i32* @ i.addr2

    store not ptr i32

    jump %for_cond_block1


%for_cond_block1
    @ load8 = load i32 i32* @ i.addr2

    @ load9 = load i32 i32* @ n.addr3

    @ sub10 = binary sub i32 @ load9 1

    @ slt11 = icmp slt i1 @ load8 @ sub10

    branch @ slt11 true: %for_suite_block4 false: %for_dest_block3


%for_suite_block4
    @ min_pos.addr12 allocate i32*

    @ load13 = load i32 i32* @ i.addr2

    store i32 @ load13 i32* @ min_pos.addr12

    @ j.addr14 allocate i32*

    jump %for_init_block5


%for_init_block5
    @ load15 = load i32 i32* @ j.addr14

    @ load16 = load i32 i32* @ i.addr2

    @ add17 = binary add i32 @ load16 1

    store not ptr i32

    jump %for_cond_block6


%for_cond_block6
    @ load18 = load i32 i32* @ j.addr14

    @ load19 = load i32 i32* @ n.addr3

    @ slt20 = icmp slt i1 @ load18 @ load19

    branch @ slt20 true: %for_suite_block9 false: %for_dest_block8


%for_suite_block9
    @ load21 = load i32* i32** @ a.addr0

    @ load22 = load i32 i32* @ j.addr14

    @ getElementPtr23 = GEP i32 i32* @ load21, i32 @ load22

    @ GEP_Load24 = load i32 i32* @ getElementPtr23

    @ load25 = load i32* i32** @ a.addr0

    @ load26 = load i32 i32* @ min_pos.addr12

    @ getElementPtr27 = GEP i32 i32* @ load25, i32 @ load26

    @ GEP_Load28 = load i32 i32* @ getElementPtr27

    @ slt29 = icmp slt i1 @ GEP_Load24 @ GEP_Load28

    branch @ slt29 true: %if_true_block10 false: %if_dest_block11


%if_true_block10
    @ load30 = load i32 i32* @ min_pos.addr12

    @ load31 = load i32 i32* @ j.addr14

    store not ptr i32

    jump %if_dest_block11


%if_dest_block11
    jump %for_incr_block7


%for_incr_block7
    @ load32 = load i32 i32* @ j.addr14

    @ prefixadd33 = binary add i32 @ load32 1

    store i32 @ prefixadd33 i32* @ j.addr14

    jump %for_cond_block6


%for_dest_block8
    @ t.addr34 allocate i32*

    @ load35 = load i32* i32** @ a.addr0

    @ load36 = load i32 i32* @ i.addr2

    @ getElementPtr37 = GEP i32 i32* @ load35, i32 @ load36

    @ GEP_Load38 = load i32 i32* @ getElementPtr37

    store i32 @ GEP_Load38 i32* @ t.addr34

    @ load39 = load i32* i32** @ a.addr0

    @ load40 = load i32 i32* @ i.addr2

    @ getElementPtr41 = GEP i32 i32* @ load39, i32 @ load40

    @ GEP_Load42 = load i32 i32* @ getElementPtr41

    @ load43 = load i32* i32** @ a.addr0

    @ load44 = load i32 i32* @ min_pos.addr12

    @ getElementPtr45 = GEP i32 i32* @ load43, i32 @ load44

    @ GEP_Load46 = load i32 i32* @ getElementPtr45

    store not ptr i32

    @ load47 = load i32* i32** @ a.addr0

    @ load48 = load i32 i32* @ min_pos.addr12

    @ getElementPtr49 = GEP i32 i32* @ load47, i32 @ load48

    @ GEP_Load50 = load i32 i32* @ getElementPtr49

    @ load51 = load i32 i32* @ t.addr34

    store not ptr i32

    jump %for_incr_block2


%for_incr_block2
    @ load52 = load i32 i32* @ i.addr2

    @ prefixadd53 = binary add i32 @ load52 1

    store i32 @ prefixadd53 i32* @ i.addr2

    jump %for_cond_block1


%for_dest_block3
    jump %selection_sort.exit


%selection_sort.exit
    return void


}

