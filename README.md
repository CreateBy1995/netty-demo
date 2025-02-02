```java
/**
 * ByteToMessageDecoder#channelRead 粘包拆包的抽象处理在这个方法，
 * 核心思想就是当接受到的消息能组成一个完整的包时将该包传递给下一个处理器（比如用户的业务处理器），
 * 否则先将数据缓存起来，等待下一个数据包
 * 至于怎么判定是一个完整的包就看具体子类的实现。比如通过定长、分隔符等、
 */

/**
 * PooledByteBufAllocator是一个 ByteBuf 分配器，实现了池化的内存管理
 * PooledByteBufAllocator是一个默认拥有16个直接内存池（DirectArena）和堆内存（HeapArena）
 */

/**
 * CompositeByteBuf 用于合并多个 ByteBuf。虽然称为合并，但实际上没有发生数据复制，而是通过移动读指针来完成数据合并，是零拷贝的一处体现。
 * 假设 buf1=[1,2,3]，读指针为0，写指针为3；buf2=[6,7,8,9]，读指针为0，写指针为4。那么，合并后的 CompositeByteBuf 读指针为0，写指针为7（3+4）。
 * 读取数据时，读指针会根据偏移量从 buf1 和 buf2 中寻找数据。例如，读指针为1时，读取的是 buf1[1]；读指针为5时，读取的是 buf2[2]（5-3，3是偏移量）
 */

/**
 * sizeIdx2sizeTab[68]  内存下标与内存大小映射表 [16B,32B,48B .....4MB] 最大也就是一个chunk的大小 
 * pageIdx2sizeTab[32]  页下标对应页大小 [8KB, 16KB, 24KB....4MB] 最大也就是一个chunk的大小 
 */

```