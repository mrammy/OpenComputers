package li.cil.oc.client.renderer.block

import li.cil.oc.client.Textures
import li.cil.oc.common.tileentity
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraftforge.common.util.ForgeDirection

object ServerRack {
  def render(rack: tileentity.ServerRack, x: Int, y: Int, z: Int, block: Block, renderer: RenderBlocks): Unit = {
    val previousRenderAllFaces = renderer.renderAllFaces
    val u1 = 1 / 16f
    val u2 = 15 / 16f
    val v1 = 2 / 16f
    val v2 = 14 / 16f
    val fs = 3 / 16f

    // Top and bottom.
    renderer.renderAllFaces = true
    renderer.setRenderBounds(0, 0, 0, 1, v1, 1)
    renderer.renderStandardBlock(block, x, y, z)
    renderer.setRenderBounds(0, v2, 0, 1, 1, 1)
    renderer.renderStandardBlock(block, x, y, z)

    // Sides.
    val front = rack.facing
    def renderSide(side: ForgeDirection, lx: Double, lz: Double, hx: Double, hz: Double) {
      if (side == front) {
        for (i <- 0 until 4 if rack.isPresent(i).isDefined) {
          side match {
            case ForgeDirection.WEST =>
              renderer.setRenderBounds(lx, v2 - (i + 1) * fs, lz + u1, u2, v2 - i * fs, hz - u1)
            case ForgeDirection.EAST =>
              renderer.setRenderBounds(u1, v2 - (i + 1) * fs, lz + u1, hx, v2 - i * fs, hz - u1)
            case ForgeDirection.NORTH =>
              renderer.setRenderBounds(lx + u1, v2 - (i + 1) * fs, lz, hx - u1, v2 - i * fs, u2)
            case ForgeDirection.SOUTH =>
              renderer.setRenderBounds(lx + u1, v2 - (i + 1) * fs, u1, hx - u1, v2 - i * fs, hz)
            case _ =>
          }
          renderer.renderStandardBlock(block, x, y, z)
        }
      }
      else {
        val isBack = front == side.getOpposite
        if (isBack) {
          renderer.setOverrideBlockTexture(Textures.ServerRack.icons(ForgeDirection.NORTH.ordinal))
        }
        renderer.setRenderBounds(lx, v1, lz, hx, v2, hz)
        renderer.renderStandardBlock(block, x, y, z)
        renderer.clearOverrideBlockTexture()
      }
    }

    renderSide(ForgeDirection.WEST, 0, 0, u1, 1)
    renderSide(ForgeDirection.EAST, u2, 0, 1, 1)
    renderSide(ForgeDirection.NORTH, 0, 0, 1, u1)
    renderSide(ForgeDirection.SOUTH, 0, u2, 1, 1)

    renderer.renderAllFaces = previousRenderAllFaces
  }
}
