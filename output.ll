string

@p
@i
@k
@n

define retType: void
parameters:  @g_init()
define retType: i32
parameters:  @main()

define retType: void
parameters:  @g_init(){
%g_init.entry
    jump %g_init.exit

%g_init.exit
    return void

}

define retType: i32
parameters:  @main(){
%main.entry
    call void @g_init()
    @n1 = load i32 i32 @n
    @funccall2 = call i32 @g_getInt()
    store not ptr i32
    @p3 = load i32 i32 @p
    @funccall4 = call i32 @g_getInt()
    store not ptr i32
    @k5 = load i32 i32 @k
    @funccall6 = call i32 @g_getInt()
    store not ptr i32
    @p7 = load i32 i32 @p
    @k8 = load i32 i32 @k
    @sub9 = binary sub i32 @p7 @k8
    @sgt10 = icmp sgt i1 @sub9 1
    branch @sgt10 true: %if_true_block0 false: %if_dest_block1

%if_true_block0
    @ConstString11 = GEP [3 x i8] [3 x i8]* << , i32 0, i32 0
    call void @g_print(i8* @ConstString11)
    jump %if_dest_block1

%if_dest_block1
    jump %for_init_block2

%for_init_block2
    @i12 = load i32 i32 @i
    @p13 = load i32 i32 @p
    @k14 = load i32 i32 @k
    @sub15 = binary sub i32 @p13 @k14
    store not ptr i32
    jump %for_cond_block3

%for_cond_block3
    @i16 = load i32 i32 @i
    @p17 = load i32 i32 @p
    @k18 = load i32 i32 @k
    @add19 = binary add i32 @p17 @k18
    @sle20 = icmp sle i1 @i16 @add19
    branch @sle20 true: %for_suite_block6 false: %for_dest_block5

%for_suite_block6
    @i21 = load i32 i32 @i
    @sle22 = icmp sle i1 1 @i21
    branch @sle22 true: %logicand_block9 false: %if_dest_block8

%logicand_block9
    @i23 = load i32 i32 @i
    @n24 = load i32 i32 @n
    @sle25 = icmp sle i1 @i23 @n24
    branch @sle25 true: %if_true_block7 false: %if_dest_block8

%if_true_block7
    @i26 = load i32 i32 @i
    @p27 = load i32 i32 @p
    @eq28 = icmp eq i1 @i26 @p27
    branch @eq28 true: %if_true_block10 false: %if_false_block11

%if_true_block10
    @ConstString29 = GEP [1 x i8] [1 x i8]* (, i32 0, i32 0
    call void @g_print(i8* @ConstString29)
    @i31 = load i32 i32 @i
    @funccall30 = call i8* @g_toString(i32 @i31)
    call void @g_print(retType: i8*
parameters:  @funccall30)
    @ConstString32 = GEP [2 x i8] [2 x i8]* ) , i32 0, i32 0
    call void @g_print(i8* @ConstString32)
    jump %if_dest_block12

%if_false_block11
    @i33 = load i32 i32 @i
    call void @g_printInt(i32 @i33)
    @ConstString34 = GEP [1 x i8] [1 x i8]*  , i32 0, i32 0
    call void @g_print(i8* @ConstString34)
    jump %if_dest_block12

%if_dest_block12
    jump %if_dest_block8

%if_dest_block8
    jump %for_incr_block4

%for_incr_block4
    @i35 = load i32 i32 @i
    @suffixadd36 = binary add i32 @i35 1
    store not ptr i32
    jump %for_cond_block3

%for_dest_block5
    @p37 = load i32 i32 @p
    @k38 = load i32 i32 @k
    @add39 = binary add i32 @p37 @k38
    @n40 = load i32 i32 @n
    @slt41 = icmp slt i1 @add39 @n40
    branch @slt41 true: %if_true_block13 false: %if_dest_block14

%if_true_block13
    @ConstString42 = GEP [3 x i8] [3 x i8]* >> , i32 0, i32 0
    call void @g_print(i8* @ConstString42)
    jump %if_dest_block14

%if_dest_block14
    @mainret_val0 move 0
    jump %main.exit
    jump %main.exit

%main.exit
    return i32 @mainret_val0

}

