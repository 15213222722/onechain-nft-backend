package io.xone.chain.onenft.move;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.onechain.OneChain;
import io.onechain.models.objects.Checkpoint;
import io.onechain.models.objects.DynamicFieldName;
import io.onechain.models.objects.OneChainObjectResponse;
import io.onechain.models.objects.PaginatedDynamicFields;
import io.onechain.models.objects.PaginatedObjectsResponse;

public class Test {

	public static String owner_address = "0x587b87bd9392dd89ae4b6437c6598d955b66d14ee625f5a2da92857bf9016fbc";
	public static String rpc_testnet_point = "https://rpc-testnet.onelabs.cc:443";
	public static String faucet_point = "https://faucet-testnet.onelabs.cc";
	public static String key_store_path = "/Users/gdan/.one/one_config/one.keystore";
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		OneChain oneChain = new OneChain(rpc_testnet_point, faucet_point, "", key_store_path);
		//水龙头领取测试币，一次1个OCT
//		CompletableFuture<FaucetResponse> faucetRes = oneChain.requestOneChainFromFaucet(owner_address);
//		System.out.println(faucetRes.get());
		//FaucetResponse{transferredGasObjects=[FaucetCoinInfo{amount=1000000000, id='0x4ccb2f1d0cd2935efa8b6a91912d8262de7fafed58f91e4dc78f1e564b7a4526', transferTxDigest='38tp1GVh4DtkxLWewiWUjDprAvZJTVxGUZKgXytCmM51'}], error='null'}

		//获取地址，需要keystore,列举所有address
//		NavigableSet<String> addresses = oneChain.addresses();
//		System.out.println(addresses);
		
//		CompletableFuture<Balance> balance = oneChain.getBalance(owner_address, "0x72eba41c73c4c2ce2bcfc6ec1dc0896ba1b5c17bfe7ae7c6c779943f84912b41::usdh::USDH");
//		System.out.println(balance.get());
		//Balance{coinType='0x72eba41c73c4c2ce2bcfc6ec1dc0896ba1b5c17bfe7ae7c6c779943f84912b41::usdh::USDH', coinObjectCount=2, totalBalance=1010000000000, lockedBalance={}}

		//获取地址所有币种余额对象
//		CompletableFuture<List<Balance>> allBalances = oneChain.getAllBalances(owner_address);
//		System.out.println(allBalances.get());
		//[Balance{coinType='0x2::oct::OCT', coinObjectCount=6, totalBalance=12998002120, lockedBalance={}}, Balance{coinType='0x72eba41c73c4c2ce2bcfc6ec1dc0896ba1b5c17bfe7ae7c6c779943f84912b41::usdh::USDH', coinObjectCount=2, totalBalance=1010000000000, lockedBalance={}}]

		//分页获取币对象
//		CompletableFuture<PaginatedCoins> allCoins = oneChain.getAllCoins(owner_address, "0x34621c3e582a61464499c43bb3448129fd1045eb2cdf035cdc2505d0ef58c946", 2);
//		System.out.println(allCoins.get());
		
		//获取币对象元数据
//		CompletableFuture<CoinMetadata> coinMetadata = oneChain.getCoinMetadata("0x72eba41c73c4c2ce2bcfc6ec1dc0896ba1b5c17bfe7ae7c6c779943f84912b41::usdh::USDH");
//		System.out.println(coinMetadata.get());
		
		//分页获取币对象集合
//		CompletableFuture<PaginatedCoins> coins = oneChain.getCoins(owner_address, "0x72eba41c73c4c2ce2bcfc6ec1dc0896ba1b5c17bfe7ae7c6c779943f84912b41::usdh::USDH", null, 2);
//		System.out.println(coins.get());
		
		//获取总供应量
//		CompletableFuture<CoinSupply> totalSupply = oneChain.getTotalSupply("0x72eba41c73c4c2ce2bcfc6ec1dc0896ba1b5c17bfe7ae7c6c779943f84912b41::usdh::USDH");
//		System.out.println(totalSupply.get());
		
		//返回指定对象的动态字段对象信息TODO
//		DynamicFieldName dynamicFieldName = new DynamicFieldName();
//		dynamicFieldName.setType("0x2::coin::Coin<0x72eba41c73c4c2ce2bcfc6ec1dc0896ba1b5c17bfe7ae7c6c779943f84912b41::usdh");
//		dynamicFieldName.setValue("");
//		CompletableFuture<OneChainObjectResponse> dynamicFieldObject = oneChain.getDynamicFieldObject("0x990cb383a210d7e7eaa8aa21307f1cca833a569237c75eb5704152823a32e826", dynamicFieldName);
//		System.out.println(dynamicFieldObject.get());
		
		//获取动态字段
//		CompletableFuture<PaginatedDynamicFields> dynamicFields = oneChain.getDynamicFields("0x990cb383a210d7e7eaa8aa21307f1cca833a569237c75eb5704152823a32e826");
//		System.out.println(dynamicFields.get());
		
		//指定地址拥有的对象列表
//		CompletableFuture<PaginatedObjectsResponse> ownedObjects = oneChain.getOwnedObjects("0x23bf8c3d7d2d55f8b78a72e3ee2d53a849c9db976ac5e8142e3ee12be4cf81d6", null, null, null);
//		System.out.println(ownedObjects.get());
		
		CompletableFuture<BigInteger> latestCheckpointSequenceNumber = oneChain.getLatestCheckpointSequenceNumber();
		BigInteger bigInteger = latestCheckpointSequenceNumber.get();
		System.out.println(bigInteger);
		CompletableFuture<Checkpoint> checkpoint = oneChain.getCheckpoint(bigInteger.toString());
		System.out.println(checkpoint.get());
		
		
	}

}
