#include<stdio.h>
int num=1;
extern const float population;
void func_extern(){
	printf("file extern.c enter,get population=%f\n",population);
}
