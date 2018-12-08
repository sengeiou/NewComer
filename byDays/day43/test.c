#include<stdio.h>
#include<limits.h>
#include<float.h>
extern int a;
int b;
int main(){
	int a=1;
	b=2;
	int c=a+b;
	printf("Hello World!\n");
	printf("int bit=%lu,size:%d~%d\n",sizeof(int),INT_MIN,INT_MAX);
	printf("char bit=%lu,size:%d~%d\n",sizeof(char),CHAR_MIN,CHAR_MAX);
	printf("float bit=%lu,size:%E~%E\n",sizeof(float),FLT_MIN,FLT_MAX);
	printf("double bit=%lu,size:%E~%E\n",sizeof(double),DBL_MIN,DBL_MAX);
	printf("long bit=%lu,size:%ld~%lu\n",sizeof(long),LONG_MIN,LONG_MAX); //2^64
	printf("c=%d\n",c);
	float d=78/3.0;
	printf("d=%f\n",d);
	return 0;
}
