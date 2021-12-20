	.globl	g_init
g_init:
LBB0:
	sw ra,-4(sp)
	sw s0,-8(sp)
	addi sp,sp,-1024
	mv s0,sp
	addi sp,sp,-1024
	j LBB1
LBB1:
	j LBB2
LBB2:
	addi sp,sp,1024
	addi sp,sp,1024
	lw s0,-8(sp)
	lw ra,-4(sp)
	ret
	.globl	main
main:
LBB3:
	sw ra,-4(sp)
	sw s0,-8(sp)
	addi sp,sp,-1024
	mv s0,sp
	addi sp,sp,-1024
	j LBB4
LBB4:
	call g_init
	lw t0,1012(s0)
	lui t0,%hi (n)
	sw t0,1012(s0)
	lw t0,1008(s0)
	lw t1,1012(s0)
	lw t0,%lo (n)(%1)
	sw t0,1008(s0)
	lw t0,1004(s0)
	li t0,1
	sw t0,1004(s0)
	lw t0,1008(s0)
	lw t1,1004(s0)
	mv t0,t1
	sw t0,1008(s0)
	lw t0,1000(s0)
	li t0,0
	sw t0,1000(s0)
	lw t0,996(s0)
	lw t1,1000(s0)
	mv t0,t1
	sw t0,996(s0)
	j LBB5
	j LBB5
LBB5:
	lw t0,996(s0)
	mv a0,t0
	sw t0,996(s0)
	addi sp,sp,1024
	addi sp,sp,1024
	lw s0,-8(sp)
	lw ra,-4(sp)
	ret
	.p2align	2
n:
	.word.	0

