#include<stdio.h>
#define AREA 960.0
extern int num;
extern void func_extern();
float max(int,int);
const float population=13.0;

int main()
{	
	func_extern();
    	printf("num=%d,area=%f\n",num,AREA);	
	func();
	float mx=max(population,AREA);
	printf("max=%f\n",mx);
    return 0;
}



int func()
{
	num = 3;
    	printf("%d\n",num);
	register int sum=0;
	for(;num<=100;num++){
		sum+=num;
	}
	const float *index= &population;
	int* ptr = &num;
	printf("sum=%d,num address=%p,population address=%p,ptr pointer=%d,index pointer=%f\n",sum,&num,&population,*ptr,*index); //寄存器没有地址,强行打印会报错
	int array[6]={8,7,4,77,99,25};
	int i;
	for(i=0;i<sizeof(array)/sizeof(array[0]);i++){ //获取数组长度=sizeof(array)/sizeof(array[0]
		printf("array[%d]=%d\n",i,array[i]);
	}
	return sum;
}

float max(int s1,int s2){
	return s1>s2?s1:s2;
}
